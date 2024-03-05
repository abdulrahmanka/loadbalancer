package com.example.lb.controller;

import com.example.lb.service.ProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
public class LbController {


    @Autowired
    ProcessingService processingService;


    @RequestMapping("/**")
    public Mono<ResponseEntity<byte[]>> proxyRequest(ServerHttpRequest request, ServerHttpResponse response) {
        log.info("Incoming request: " + request.getURI().getRawPath());
        return processingService.relay(request, response);

    }
}
