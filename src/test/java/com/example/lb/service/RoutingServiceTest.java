package com.example.lb.service;

import com.example.lb.utils.CommonTestUtil;
import com.example.lb.model.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class RoutingServiceTest {

    @InjectMocks
    @Autowired
    private RoutingService routingService;

    @Mock
    IServerGroupService serverGroupService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup());
        ReflectionTestUtils.setField(routingService, "serverGroupService", serverGroupService);
    }


    @Test
    public void testGetServer() {
        Server server = routingService.getServer("profile");
        assertNotNull(server);
        assertEquals("http://localhost:8081", server.getUrl());
        assertTrue(server.isHealthy());
    }


}
