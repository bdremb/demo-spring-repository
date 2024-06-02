package ru.learn.skill.demo.spring.jwt.auth.example.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learn.skill.demo.spring.jwt.auth.example.entity.RoleType;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String email;
    private Set<RoleType> roles;
    private String password;
}
