package com.example.lb.utils;

import com.example.lb.model.HealthCheck;
import com.example.lb.model.RetryStrategy;
import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;

import java.util.ArrayList;
import java.util.List;

public class CommonTestUtil {

    public static ServerGroup getServerGroup() {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setRootPath("profile");
        serverGroup.setRoutingAlgorithm("RoundRobin");

        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setStrategy("Fixed");
        retryStrategy.setMaxRetries(2);
        retryStrategy.setDuration(1);
        serverGroup.setRetryStrategy(retryStrategy);

        HealthCheck healthCheck = new HealthCheck();
        healthCheck.setPath("");
        serverGroup.setHealthCheck(healthCheck);

        List<Server> servers = new ArrayList<>();
        Server server = new Server("http://localhost:8081", true);
        servers.add(server);

        serverGroup.setServers(servers);
        return serverGroup;
    }

    public static ServerGroup getServerGroup2() {
        ServerGroup serverGroup = new ServerGroup();
        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setStrategy("exponential");
        retryStrategy.setMaxRetries(2);
        retryStrategy.setDuration(1);
        serverGroup.setRetryStrategy(retryStrategy);
        return serverGroup;
    }



}
