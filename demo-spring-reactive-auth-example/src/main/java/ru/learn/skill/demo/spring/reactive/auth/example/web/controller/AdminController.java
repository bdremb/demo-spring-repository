package ru.learn.skill.demo.spring.reactive.auth.example.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> getAdminInfo(Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .map(name -> ResponseEntity.ok("Method getAdminInfo calling. Admin username is: " + name));
    }

}
