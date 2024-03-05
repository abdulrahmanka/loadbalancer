package com.example.lb.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class ServerGroup {

    private String rootPath;
    private String routingAlgorithm;

    private RetryStrategy retryStrategy;
    private HealthCheck healthCheck;

    private List<Server> servers;

    public List<Server> getHealthyServers() {
        return servers.stream()
                .filter(Server::isHealthy)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerGroup that)) return false;
        return Objects.equals(getRootPath(), that.getRootPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRootPath());
    }
}
