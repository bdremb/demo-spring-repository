package com.example.spring.demo.spring.integration.app.entity;

import com.example.spring.demo.spring.integration.app.model.EntityModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DatabaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Instant date;

    public static DatabaseEntity from(EntityModel model) {
        return new DatabaseEntity(model.getId(), model.getName(), model.getDate());
    }

}
