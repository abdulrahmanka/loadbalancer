package com.example.lb.service;

import com.example.lb.model.ServerGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class ServerGroupServiceTest {

    @InjectMocks
    @Autowired
    ServerGroupService serverGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void init(){
        //server group file is loaded without errors
        assertNotNull(serverGroupService);
    }

    @Test
    void initError()  {
        ReflectionTestUtils.setField(serverGroupService, "serverGroupFile", "server-group-error.json");
        assertThrows(RuntimeException.class, () -> serverGroupService.init());
    }

    @Test
    void testServerGroup() throws IOException {
        serverGroupService.init();
       ServerGroup serverGroup = serverGroupService.getServerGroup("clients");
       assertEquals("clients", serverGroup.getRootPath());
       assertEquals(2, serverGroup.getServers().size());
       assertEquals("http://localhost:8081", serverGroup.getServers().get(0).getUrl());
       assertEquals("http://localhost:8082", serverGroup.getServers().get(1).getUrl());
       assertTrue(serverGroup.getServers().get(0).isHealthy());
       assertTrue(serverGroup.getServers().get(1).isHealthy());

        serverGroup = serverGroupService.getServerGroup("profile");
        assertEquals("profile", serverGroup.getRootPath());
    }

    @Test
    void testServerGroupError() {
        assertThrows(RuntimeException.class, () -> serverGroupService.getServerGroup("error"));
    }

    @Test
    void testServerGroupNull() {
        assertThrows(RuntimeException.class, () -> serverGroupService.getServerGroup(null));
    }


    @Test
    void testServerGroups() {
        assertEquals(2, serverGroupService.getServerGroups("RoundRobin").size());
    }


}