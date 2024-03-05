package com.example.lb.algo;

import com.example.lb.model.Server;

public interface ILbRouting {

    String getAlgorithmName();

    Server getServer(String path);


}
