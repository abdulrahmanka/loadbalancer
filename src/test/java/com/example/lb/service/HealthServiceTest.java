package com.example.lb.service;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import com.example.lb.utils.CommonTestUtil;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HealthServiceTest {

    @InjectMocks
    HealthService healthService;

    @Mock
    IServerGroupService serverGroupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup());
        ReflectionTestUtils.setField(healthService, "serverGroupService", serverGroupService);
    }

    @Test
    void isServerHealthyFail() {
       ServerGroup serverGroup = CommonTestUtil.getServerGroup();
       Boolean result =  healthService.isServerHealthy(serverGroup, new Server("http://localhost:98765", true));
       assertNotNull(!result);
    }

    @Test
    void isServerHealthy() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new okhttp3.mockwebserver.MockResponse()
                .setBody("Test Response").addHeader("Content-Type", "text/plain"));
        server.start();
        int port = server.getPort();
        ServerGroup serverGroup = CommonTestUtil.getServerGroup();
        Boolean result =  healthService.isServerHealthy(serverGroup, new Server("http://localhost:"+port, true));
        assertNotNull(result);
        server.shutdown();
    }
}