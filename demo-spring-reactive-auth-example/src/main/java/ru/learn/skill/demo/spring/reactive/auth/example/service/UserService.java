package ru.learn.skill.demo.spring.reactive.auth.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.Role;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.User;
import ru.learn.skill.demo.spring.reactive.auth.example.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User createNewAccount(User user, Role role) {
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found by username: " + username));
    }

}
