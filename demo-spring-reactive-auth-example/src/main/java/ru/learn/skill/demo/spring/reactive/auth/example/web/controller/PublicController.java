package ru.learn.skill.demo.spring.reactive.auth.example.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.Role;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.RoleType;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.User;
import ru.learn.skill.demo.spring.reactive.auth.example.service.UserService;
import ru.learn.skill.demo.spring.reactive.auth.example.web.model.UserDto;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;

    @GetMapping
    public Mono<ResponseEntity<String>> publicMethod() {
        return Mono.just(ResponseEntity.ok("Public method calling"));
    }

    @PostMapping("/account")
    public Mono<ResponseEntity<UserDto>> createUserAccount(@RequestBody UserDto userDto, @RequestParam RoleType roleType) {
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(createAccount(userDto, roleType))
        );
    }

    private UserDto createAccount(UserDto userDto, RoleType roleType) {
        var user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        var createdUser = userService.createNewAccount(user, Role.from(roleType));

        return UserDto.builder()
                .username(createdUser.getUsername())
                .password(createdUser.getPassword())
                .build();
    }

}
