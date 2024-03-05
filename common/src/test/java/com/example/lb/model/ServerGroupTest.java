package com.example.lb.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerGroupTest {

    @Test
    void testGetHealthyServers() {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setHealthCheck(new HealthCheck());
        List<Server> servers = List.of(new Server("a", true), new Server("b", false));
        serverGroup.setServers(servers);
        assertEquals(1, serverGroup.getHealthyServers().size());
    }

    @Test
    void testEquality(){
        ServerGroup serverGroup1 = new ServerGroup();
        serverGroup1.setRootPath("test");
        ServerGroup serverGroup2 = new ServerGroup();
        serverGroup2.setRootPath("test");
        assertEquals(serverGroup1, serverGroup2);
        assertEquals(serverGroup1.hashCode(), serverGroup2.hashCode());
    }
}