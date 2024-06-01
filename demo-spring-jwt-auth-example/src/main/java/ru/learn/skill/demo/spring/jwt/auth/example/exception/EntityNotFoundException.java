package ru.learn.skill.demo.spring.jwt.auth.example.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
