package ru.learn.skill.demo.spring.client.auth.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.client.auth.example.web.model.AuthRequest;

@Component
@RequiredArgsConstructor
public class BasicAuthClient {

    private final WebClient defaultWebClient;

    public Mono<String> getData(AuthRequest authRequest) {
        return defaultWebClient.get()
                .uri("/api/v1/user")
                .headers(httpHeader -> httpHeader.setBasicAuth(authRequest.getUsername(), authRequest.getPassword()))
                .retrieve()
                .bodyToMono(String.class);
    }
}
