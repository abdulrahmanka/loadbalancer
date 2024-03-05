package com.example.lb.service;

import com.example.lb.model.Server;
import com.example.lb.model.ServerGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ServerGroupService implements IServerGroupService {


    private final ConcurrentHashMap<String, ServerGroup> serverGroupMap = new ConcurrentHashMap<>();

    @Value("${serverGroupFile}")
    private String serverGroupFile;


    @PostConstruct
    void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(serverGroupFile);
            String jsonContent;
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    jsonContent = new String(fis.readAllBytes());
                }
            }else{
                //Checking for classpath resource
                ClassPathResource resource = new ClassPathResource(serverGroupFile);
                jsonContent = new String(resource.getInputStream().readAllBytes());
            }
            List<ServerGroup> serverGroups = objectMapper.readValue(jsonContent,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ServerGroup.class));
            for (ServerGroup serverGroup : serverGroups) {
                serverGroupMap.put(serverGroup.getRootPath(), serverGroup);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while reading server group file", e);
        }
    }

    @Override
    public ServerGroup getServerGroup(String rootPath) {
        ServerGroup serverGroup = serverGroupMap.get(rootPath);
        if (serverGroup == null) {
            throw new RuntimeException("No server group found for root path: " + rootPath);
        }
        return serverGroup;
    }


    @Override
    public List<ServerGroup> getServerGroups(String routingAlgorithm) {
        return serverGroupMap.values().stream()
                .filter(sg -> sg.getRoutingAlgorithm().equals(routingAlgorithm))
                .toList();
    }

    @Override
    public Collection<ServerGroup> getAllServerGroups() {
        return serverGroupMap.values();
    }

    public void updateServerHealth(Server server, String rootPath, Boolean isHealthy) {
        ServerGroup serverGroup = serverGroupMap.get(rootPath);
        if (serverGroup != null) {
            serverGroup.getServers().stream()
                    .filter(s -> s.equals(server))
                    .forEach(s -> s.setHealthy(isHealthy));
        }
    }
}
