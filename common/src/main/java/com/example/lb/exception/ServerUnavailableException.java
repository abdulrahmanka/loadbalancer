package com.example.lb.exception;

public class ServerUnavailableException extends RuntimeException{
    public ServerUnavailableException(String message) {
        super(message);
    }
}
