package ru.learn.skill.demo.spring.basic.auth.example.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.learn.skill.demo.spring.basic.auth.example.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);

}
