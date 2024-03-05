package com.example.lb.service;

import com.example.lb.model.Server;

public interface IRoutingService {
    Server getServer(String rootPath);

}
