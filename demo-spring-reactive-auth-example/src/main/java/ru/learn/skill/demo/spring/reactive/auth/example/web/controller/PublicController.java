package ru.learn.skill.demo.spring.reactive.auth.example.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @GetMapping
    public Mono<ResponseEntity<String>> publicMethod() {
        return Mono.just(ResponseEntity.ok("Public method calling"));
    }

}
