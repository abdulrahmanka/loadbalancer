package com.example.lb.service;

import com.example.lb.algo.ILbRouting;
import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RoutingService implements IRoutingService {


    @Value("#{'${routing.algos}'.split(',')}")
    private List<String> algoBeans;

    @Autowired
    IServerGroupService serverGroupService;



    Map<String, ILbRouting> routingAlgorithms = new HashMap<>();


    private ILbRouting initAlgoClass(String algoClassName){
        try {
            Class<?> clazz = Class.forName(algoClassName);
            Constructor<?> constructor = clazz.getConstructor(IServerGroupService.class);
            Object instance = constructor.newInstance(serverGroupService);
            ILbRouting routingAlgo = (ILbRouting) instance;
            return routingAlgo;
        } catch (ClassNotFoundException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    @PostConstruct
    public void init() {
        for(String algoBean: algoBeans){
            ILbRouting roundRobin = initAlgoClass(algoBean);
            List<ServerGroup> serverGroups =
                    serverGroupService.getServerGroups(roundRobin.getAlgorithmName());
            serverGroups.forEach(sg -> routingAlgorithms.put(sg.getRootPath(), roundRobin));
        }
    }

    @Override
    public Server getServer(String rootPath) {
        ILbRouting routingAlgo =  routingAlgorithms.get(rootPath);
        Server server =  routingAlgo.getServer(rootPath);
        log.info("Server selected: " + server.getUrl()+" for path: "+rootPath+" using algo: "
                +routingAlgo.getAlgorithmName());
        return server;
    }


}
