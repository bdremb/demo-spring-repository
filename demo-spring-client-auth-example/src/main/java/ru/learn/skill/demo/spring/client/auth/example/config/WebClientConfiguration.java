package ru.learn.skill.demo.spring.client.auth.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.client.auth.example.client.TokenProvider;

@Configuration
@Slf4j
public class WebClientConfiguration {

    @Bean
    public WebClient defaultWebClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public WebClient jwtWebClient(TokenProvider tokenProvider) {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter((request, next) -> addBearerToken(request, next, tokenProvider))
                .filter((request, next) -> retryUnauthorized(request, next, tokenProvider))
                .build();
    }


    private Mono<ClientResponse> retryUnauthorized(ClientRequest request, ExchangeFunction next, TokenProvider tokenProvider) {
        return next.exchange(request)
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                        log.info("401 error. Get token and send request again");
                        return tokenProvider.getToken(false)
                                .flatMap(
                                        token -> {
                                            var retryRequest = ClientRequest.from(request)
                                                    .headers(httpHeader -> httpHeader.setBearerAuth(token))
                                                    .build();
                                            return next.exchange(retryRequest);
                                        }
                                );
                    }
                    return next.exchange(request);
                });
    }

    private Mono<ClientResponse> addBearerToken(ClientRequest request, ExchangeFunction next, TokenProvider tokenProvider) {
        return tokenProvider.getToken(true)
                .flatMap(token -> next.exchange(
                        ClientRequest.from(request)
                                .headers(httpHeader -> httpHeader.setBearerAuth(token))
                                .build()
                ));
    }

}
