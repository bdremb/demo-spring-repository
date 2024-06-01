package ru.learn.skill.demo.spring.jwt.auth.example.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
