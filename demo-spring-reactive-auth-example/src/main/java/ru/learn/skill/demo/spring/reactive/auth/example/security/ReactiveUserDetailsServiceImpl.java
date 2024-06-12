package ru.learn.skill.demo.spring.reactive.auth.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.reactive.auth.example.service.UserService;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(()-> userService.findUserByUsername(username))
                .flatMap(Mono::just)
                .map(AppUserPrincipal::new);
    }

}
