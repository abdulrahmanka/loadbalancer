package com.example.lb.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    @Test
    public void testEquality(){
        Server server1 = new Server();
        server1.setUrl("test");
        Server server2 = new Server();
        server2.setUrl("test");
        assertEquals(server1, server2);
        assertEquals(server1.hashCode(), server2.hashCode());

    }
}
