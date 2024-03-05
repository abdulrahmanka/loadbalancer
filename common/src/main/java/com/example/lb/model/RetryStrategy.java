package com.example.lb.model;

import lombok.Data;

@Data
public class RetryStrategy {

    private String strategy;
    private int maxRetries;
    private int duration;
}
