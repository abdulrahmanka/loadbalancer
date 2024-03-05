package com.example.lb.service;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;

public interface IHealthService {
    Boolean isServerHealthy(ServerGroup serverGroup, Server server);

}
