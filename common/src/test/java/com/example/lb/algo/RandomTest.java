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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class RandomTest {



    @Mock
    IServerGroupService serverGroupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup());
    }

    @Test
    void getAlgorithmName() {
        Random random = new Random(null);
        assertEquals(random.getAlgorithmName(), "Random");
    }

    @Test
    void getServer1() {
        Random random = new Random(null);
        assertThrows(RuntimeException.class, () -> random.getServer("path"));
    }

    @Test
    void getServer2(){
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup());
        Random random = new Random(serverGroupService);
        assertNotNull(random.getServer("profile"));
    }

    @Test
    void getServer3(){
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(getServerGroup2());
        Random random = new Random(serverGroupService);
        assertThrows(RuntimeException.class, () -> random.getServer("path"));
    }


    public static ServerGroup getServerGroup() {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setRootPath("profile");
        serverGroup.setRoutingAlgorithm("Random");
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
        serverGroup.setRoutingAlgorithm("Random");
        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setStrategy("Fixed");
        retryStrategy.setMaxRetries(2);
        retryStrategy.setDuration(1);
        serverGroup.setRetryStrategy(retryStrategy);
        List<Server> servers = new ArrayList<>();
        Server server = new Server("http://localhost:8081", false);
        servers.add(server);

        serverGroup.setServers(servers);
        return serverGroup;
    }
}