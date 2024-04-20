package com.example.spring.demo.spring.integration.app.model;

import com.example.spring.demo.spring.integration.app.entity.DatabaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityModel {

    private UUID id;

    private String name;

    private Instant date;

    public static EntityModel from(DatabaseEntity entity) {
        return new EntityModel(entity.getId(), entity.getName(), entity.getDate());
    }

}
