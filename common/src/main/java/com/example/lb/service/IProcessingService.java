package com.example.lb.service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface IProcessingService {

    Mono<ResponseEntity<byte[]>> relay(ServerHttpRequest request,
                                       ServerHttpResponse response);

    Mono<ResponseEntity<byte[]>> relayRequest(ServerHttpRequest request,
                                              ServerHttpResponse response,
                                              String rootPath
    );
}
