package ru.learn.skill.demo.spring.jwt.auth.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.learn.skill.demo.spring.jwt.auth.example.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
