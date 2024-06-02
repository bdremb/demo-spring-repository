package ru.learn.skill.demo.spring.jwt.auth.example.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learn.skill.demo.spring.jwt.auth.example.exception.AlreadyExistsException;
import ru.learn.skill.demo.spring.jwt.auth.example.repository.UserRepository;
import ru.learn.skill.demo.spring.jwt.auth.example.security.SecurityService;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.AuthResponse;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.CreateUserRequest;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.LoginRequest;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.RefreshTokenRequest;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.RefreshTokenResponse;
import ru.learn.skill.demo.spring.jwt.auth.example.web.model.SimpleResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final SecurityService securityService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists!");
        }
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists!");
        }

        securityService.register(createUserRequest);
        return ResponseEntity.ok(new SimpleResponse("User created!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(securityService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();
        return ResponseEntity.ok(new SimpleResponse("User logout. Username is: " + userDetails.getUsername()));
    }

}
