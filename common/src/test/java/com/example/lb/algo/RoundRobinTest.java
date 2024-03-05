package com.example.lb.algo;

import com.example.lb.model.RetryStrategy;
import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import com.example.lb.service.IServerGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class RoundRobinTest {


    @Mock
    IServerGroupService serverGroupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup());
    }

    @Test
    void getAlgorithmName() {
        RoundRobin roundRobin = new RoundRobin(null);
        assertEquals(roundRobin.getAlgorithmName(), "RoundRobin");
    }

    @Test
    void getServer1() {
        RoundRobin roundRobin = new RoundRobin(null);
        assertThrows(RuntimeException.class, () -> roundRobin.getServer("path"));
    }

    @Test
    void getServer2() {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup());
        RoundRobin roundRobin = new RoundRobin(serverGroupService);
        assertNotNull(roundRobin.getServer("profile"));
    }

    @Test
    void getServer3() {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup2());
        RoundRobin roundRobin = new RoundRobin(serverGroupService);
        Server server1 = roundRobin.getServer("profile");
        Server server2 = roundRobin.getServer("profile");
        assertFalse(Objects.equals(server1.getUrl(), server2.getUrl()));
    }


    public static ServerGroup getServerGroup() {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setRootPath("profile");
        serverGroup.setRoutingAlgorithm("RoundRobin");
        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setStrategy("Fixed");
        retryStrategy.setMaxRetries(2);
        retryStrategy.setDuration(1);
        serverGroup.setRetryStrategy(retryStrategy);
        List<Server> servers = new ArrayList<>();
        Server server = new Server("http://localhost:8081", true);
        servers.add(server);

        serverGroup.setServers(servers);
        return serverGroup;
    }

    public static ServerGroup getServerGroup2() {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setRootPath("profile");
        serverGroup.setRoutingAlgorithm("RoundRobin");
        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setStrategy("Fixed");
        retryStrategy.setMaxRetries(2);
        retryStrategy.setDuration(1);
        serverGroup.setRetryStrategy(retryStrategy);
        List<Server> servers = new ArrayList<>();
        Server server = new Server("http://localhost:8081", true);
        servers.add(server);
        server = new Server("http://localhost:8082", true);
        servers.add(server);
        serverGroup.setServers(servers);
        return serverGroup;
    }
}