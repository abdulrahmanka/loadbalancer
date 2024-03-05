package com.example.lb.service;

import com.example.lb.exception.ServerUnavailableException;
import com.example.lb.model.Server;
import com.example.lb.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class ProcessingService implements IProcessingService {


    @Autowired
    IRetryService retryService;

    @Autowired
    IRoutingService routingService;

    @Autowired
    IServerGroupService serverGroupService;

    private final WebClient webClient = WebClient.create();


    public Mono<ResponseEntity<byte[]>> relay(ServerHttpRequest request,
                                                                   ServerHttpResponse response) {
        String rootPath = CommonUtils.getRootPath(request);
        Retry retryStrategy = retryService.retry(rootPath);

        return Mono.defer(() -> relayRequest(request, response, rootPath))
                .retryWhen(retryStrategy)
                .onErrorResume(Exception.class,
                        e -> Mono.error(new ServerUnavailableException(e.getMessage())));
    }


    public Mono<ResponseEntity<byte[]>> relayRequest(ServerHttpRequest request,
                                                     ServerHttpResponse response,
                                                     String rootPath
    ) {
        Server server = routingService.getServer(rootPath);
        if (server == null) {
            log.error("No server found for root path: " + rootPath);
            return Mono.just(ResponseEntity.status(503).build());
        }

        HttpMethod method = request.getMethod();
        BodyInserter<?, ? super ReactiveHttpOutputMessage> bodyInserter
                = BodyInserters.fromDataBuffers(request.getBody());

        return webClient
                .method(method)
                .uri(server.getUrl() + request.getURI().getRawPath())
                .headers(headers -> headers.addAll(request.getHeaders()))
                .body(bodyInserter)
                .exchangeToMono(clientResponse -> clientResponse.toEntity(byte[].class))
                .doOnError(error -> {
                    log.info("Error occurred while relaying request to server: " + server.getUrl());
                    log.debug("Error occurred while relaying request to server: " + server.getUrl(), error);
                    serverGroupService.updateServerHealth(server, rootPath, Boolean.FALSE);
                })
                .doOnNext(clientResponse -> {
                    log.debug("Response received from server: " + server.getUrl() + " with status: " +
                            clientResponse.getStatusCode());
                    response.getHeaders().addAll(clientResponse.getHeaders());
                });

    }
}
