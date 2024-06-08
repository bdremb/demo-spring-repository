package ru.learn.skill.demo.spring.client.auth.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient defaultWebClient() {
        return WebClient.create("http://localhost:8080");
    }

}
