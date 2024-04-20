package com.example.spring.demo.spring.integration.app.repository;

import com.example.spring.demo.spring.integration.app.entity.DatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DatabaseEntityRepository extends JpaRepository<DatabaseEntity, UUID> {
}
