package ru.server.example.demo.spring.app.client.exception;

public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
