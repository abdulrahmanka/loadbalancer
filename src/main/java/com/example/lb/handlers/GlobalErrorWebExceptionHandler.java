package com.example.lb.handlers;

import com.example.lb.exception.ServerUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorWebExceptionHandler {

    @ExceptionHandler(ServerUnavailableException.class)
    public Mono<ResponseEntity<String>> handleServerUnavailable(ServerUnavailableException ex) {
        // Customize the response as needed
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Backend Server Unavailable: " + ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<String>> handleRuntime(RuntimeException ex) {
        // Customize the response as needed
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage()));
    }

}
