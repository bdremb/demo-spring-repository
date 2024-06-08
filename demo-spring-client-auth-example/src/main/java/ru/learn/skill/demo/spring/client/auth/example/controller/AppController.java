package ru.learn.skill.demo.spring.client.auth.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.client.auth.example.clien.BasicAuthClient;
import ru.learn.skill.demo.spring.client.auth.example.web.model.AuthRequest;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class AppController {

    private final BasicAuthClient basicAuthClient;

    @PostMapping
    public Mono<String> authRequest(@RequestBody AuthRequest authRequest) {
        return basicAuthClient.getData(authRequest);
    }

}
