package ru.learn.skill.demo.spring.client.auth.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthClient {

    private final WebClient jwtWebClient;

    public Mono<String> getData() {
        return jwtWebClient.get()
                .uri("/api/v1/app/user")
                .retrieve()
                .bodyToMono(String.class);
    }
}
