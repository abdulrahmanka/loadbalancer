package com.example.lb.service;

import com.example.lb.exception.ServerUnavailableException;
import com.example.lb.model.Server;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessingServiceTest {

    @InjectMocks
    ProcessingService processingService;

    @Mock
    RetryService retryService;

    @Mock
    RoutingService routingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(retryService.retry(any(String.class))).thenReturn(Retry.fixedDelay(2, Duration.ofSeconds(1)));
        ReflectionTestUtils.setField(processingService, "retryService", retryService);
        ReflectionTestUtils.setField(processingService, "routingService", routingService);
    }

    @Test
    public void testRelayNoEndpoints() {
        when(routingService.getServer(any(String.class))).thenReturn(new Server("http://localhost:9000", true));

        MockServerHttpRequest mockRequest = MockServerHttpRequest
                .get("http://localhost:9000/path")
                .build();

        Mono<ResponseEntity<byte[]>> result = processingService.relay(mockRequest, null);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ServerUnavailableException)
                .verify();

    }

    @Test
    public void testRelay() throws IOException {

        try(MockWebServer server = new MockWebServer()) {

            server.enqueue(new okhttp3.mockwebserver.MockResponse()
                    .setBody("Test Response").addHeader("Content-Type", "text/plain"));
            server.start();
            int port = server.getPort();
            String url = "http://localhost:" + port;
            when(routingService.getServer(any(String.class))).thenReturn(new Server(url, true));

            MockServerHttpRequest mockRequest = MockServerHttpRequest
                    .get("http://localhost:9000/path")
                    .build();
            MockServerHttpResponse mockResponse = new MockServerHttpResponse();

            Mono<ResponseEntity<byte[]>> result = processingService.relay(mockRequest, mockResponse);

            StepVerifier.create(result)
                    .expectNextMatches(responseEntity -> responseEntity.getStatusCode().value() == 200)
                    .verifyComplete();
            server.shutdown();
        }
    }


    @Test
    public void testRelayPost() throws IOException {

        try(MockWebServer server = new MockWebServer()) {

            server.enqueue(new okhttp3.mockwebserver.MockResponse()
                    .setBody("Test Response").addHeader("Content-Type", "text/plain"));
            server.start();
            int port = server.getPort();
            String url = "http://localhost:" + port;
            when(routingService.getServer(any(String.class))).thenReturn(new Server(url, true));

            MockServerHttpRequest mockRequest = MockServerHttpRequest
                    .post("http://localhost:9000/path")
                    .build();
            MockServerHttpResponse mockResponse = new MockServerHttpResponse();

            Mono<ResponseEntity<byte[]>> result = processingService.relay(mockRequest, mockResponse);

            StepVerifier.create(result)
                    .expectNextMatches(responseEntity -> responseEntity.getStatusCode().value() == 200)
                    .verifyComplete();
            server.shutdown();
        }
    }

    @Test
    public void testServerUnavailableException() {
        MockServerHttpRequest mockRequest = MockServerHttpRequest
                .get("/path")
                .build();
        when(routingService.getServer(any(String.class))).thenReturn(null);
        Mono<ResponseEntity<byte[]>> result = processingService.relay(mockRequest, null);


        StepVerifier.create(result)
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode().value() == 503)
                .verifyComplete();
    }

}