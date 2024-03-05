package com.example.lb.service;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;

import java.util.Collection;
import java.util.List;

public interface IServerGroupService {

    ServerGroup getServerGroup(String rootPath);

    List<ServerGroup> getServerGroups(String routingAlgorithm);

    Collection<ServerGroup> getAllServerGroups();

    void updateServerHealth(Server server, String rootPath, Boolean isHealthy);
}
