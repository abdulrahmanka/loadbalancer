package com.example.lb.algo;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import com.example.lb.service.IServerGroupService;

import java.util.List;

public class Random implements ILbRouting{
    private final IServerGroupService serverGroupService;

    public Random(IServerGroupService serverGroupService) {
        this.serverGroupService = serverGroupService;
    }

    @Override
    public String getAlgorithmName() {
        return "Random";
    }

    @Override
    public Server getServer(String path) {
        ServerGroup serverGroup = serverGroupService.getServerGroup(path);
        if (serverGroup == null) {
            throw new RuntimeException("No server group found for root path: " + path);
        }
        List<Server> servers = serverGroup.getHealthyServers();
        if (!servers.isEmpty()) {
            int index = (int) (Math.random() * servers.size());
            return servers.get(index);
        }else{
            throw new RuntimeException("No healthy server found for root path: " + path);
        }
    }
}
