package com.example.lb.algo;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import com.example.lb.service.IServerGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundRobin implements ILbRouting {

    private final IServerGroupService serverGroupService;


    Map<String, Integer> indexMap = new HashMap<>();

    public RoundRobin(IServerGroupService serverGroupService) {
        this.serverGroupService = serverGroupService;
    }

    @Override
    public String getAlgorithmName() {
        return "RoundRobin";
    }



    @Override
    public Server getServer(String path) {
        ServerGroup serverGroup = serverGroupService.getServerGroup(path);
        if (serverGroup == null) {
            throw new RuntimeException("No server group found for root path: " + path);
        }
        List<Server> servers = serverGroup.getHealthyServers();
        if (servers.isEmpty()) {
            throw new RuntimeException("No healthy server found for root path: " + path);
        }
        int index = indexMap.getOrDefault(path, 0);
        if (index >= servers.size()) {
            index = 0;
        }
        indexMap.put(path, index + 1);
        return servers.get(index);
    }


}
