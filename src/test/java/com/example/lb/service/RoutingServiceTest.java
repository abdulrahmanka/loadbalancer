package com.example.lb.service;

import com.example.lb.model.Server;
import com.example.lb.utils.CommonTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class RoutingServiceTest {

    @InjectMocks
    @Autowired
    private RoutingService routingService;

    @Mock
    IServerGroupService serverGroupService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(routingService, "serverGroupService", serverGroupService);
        List<String> algoBeans = List.of("com.example.lb.algo.RoundRobin","com.example.lb.algo.Random");
        ReflectionTestUtils.setField(routingService, "algoBeans", algoBeans);
    }


    @Test
    public void testGetServer() {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup());
        when(serverGroupService.getServerGroups(any(String.class))).thenReturn(List.of(CommonTestUtil.getServerGroup()));
        routingService.init();
        Server server = routingService.getServer("profile");
        assertNotNull(server);
        assertEquals("http://localhost:8081", server.getUrl());
        assertTrue(server.isHealthy());
    }
    @Test
    public void testGetServerFail() {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup());
        when(serverGroupService.getServerGroups(any(String.class))).thenReturn(List.of(CommonTestUtil.getServerGroup()));
        routingService.init();
        assertThrows(RuntimeException.class, () -> routingService.getServer("nothing"));
    }



}
