package com.example.lb.service;


import com.example.lb.model.ServerGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.time.Duration;


@Component
public class RetryService implements IRetryService{

    @Autowired
    IServerGroupService serverGroupService;



    public Retry retry(String rootPath) {
        ServerGroup serverGroup = serverGroupService.getServerGroup(rootPath);
        if(serverGroup.getRetryStrategy() == null){
            return Retry.fixedDelay(3, Duration.ofSeconds(1));
        }
        return switch (serverGroup.getRetryStrategy().getStrategy().toLowerCase()) {
            case "fixed" -> Retry.fixedDelay(serverGroup.getRetryStrategy().getMaxRetries(),
                    Duration.ofSeconds(serverGroup.getRetryStrategy().getDuration()));
            case "exponential" -> Retry.backoff(serverGroup.getRetryStrategy().getMaxRetries(),
                    Duration.ofSeconds(serverGroup.getRetryStrategy().getDuration()));
            default -> Retry.fixedDelay(3, Duration.ofSeconds(1));
        };
    }

}
