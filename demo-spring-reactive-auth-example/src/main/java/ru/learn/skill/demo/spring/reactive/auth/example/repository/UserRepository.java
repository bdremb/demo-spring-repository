package ru.learn.skill.demo.spring.reactive.auth.example.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.learn.skill.demo.spring.reactive.auth.example.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //15.30
    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserByUsername(String username);

}
