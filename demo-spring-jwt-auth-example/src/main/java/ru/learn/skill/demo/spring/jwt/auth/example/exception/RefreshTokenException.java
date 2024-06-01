package ru.learn.skill.demo.spring.jwt.auth.example.exception;

import java.text.MessageFormat;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String token, String message) {
        super(MessageFormat.format("Error trying to refresh by token: {0} : {1}", token, message));
    }
}
