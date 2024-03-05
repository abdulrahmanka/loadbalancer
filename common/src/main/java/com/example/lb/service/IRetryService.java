package com.example.lb.service;

import reactor.util.retry.Retry;

public interface IRetryService {
    Retry retry(String rootPath);



}
