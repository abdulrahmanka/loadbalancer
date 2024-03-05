package com.example.lb.service;

import com.example.lb.model.HealthCheck;
import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class HealthService implements IHealthService {

    OkHttpClient client = new OkHttpClient();

    @Autowired
    IServerGroupService serverGroupService;

    @PostConstruct
    public void init() {
        Collection<ServerGroup> serverGroups = serverGroupService.getAllServerGroups();
        for (ServerGroup serverGroup : serverGroups) {
            for (Server server : serverGroup.getServers()) {
                isServerHealthy(serverGroup, server);
            }
        }
    }


    public Boolean isServerHealthy(ServerGroup serverGroup, Server server){
        HealthCheck healthCheck = serverGroup.getHealthCheck();
        String url = server.getUrl() + healthCheck.getPath();
        try {
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(url)
                            .build();
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    serverGroupService.updateServerHealth(server, serverGroup.getRootPath(), Boolean.TRUE);
                    return Boolean.TRUE;
                } else {
                    log.info("Server is unhealthy: " + server.getUrl());
                    serverGroupService.updateServerHealth(server, serverGroup.getRootPath(), Boolean.FALSE);
                    return Boolean.FALSE;
                }
            }
        } catch (Exception e) {
                    serverGroupService.updateServerHealth(server, serverGroup.getRootPath(), Boolean.FALSE);
                    log.error("Error occurred while checking server health: " + server.getUrl(), e);
                    return Boolean.FALSE;
                }

    }


    @Scheduled(fixedRate = 10000)
    public void doHealthCheck() {
        log.debug("health check started");
        init();
    }


}
