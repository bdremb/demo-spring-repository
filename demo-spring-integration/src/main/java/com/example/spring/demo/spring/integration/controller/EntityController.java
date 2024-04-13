package com.example.spring.demo.spring.integration.controller;

import com.example.spring.demo.spring.integration.model.EntityModel;
import com.example.spring.demo.spring.integration.model.UpsertEntityRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/entity")
public class EntityController {

    @GetMapping
    public ResponseEntity<List<EntityModel>> entityList() {
        List<EntityModel> entityModels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            entityModels.add(EntityModel.createMockModel("Model: " + (i + 1)));
        }

        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/{name}")
    public ResponseEntity<EntityModel> entityByName(@PathVariable String name) {
        return ResponseEntity.ok(EntityModel.createMockModel(name));
    }

    @PostMapping
    public ResponseEntity<EntityModel> createEntity (@RequestBody UpsertEntityRequest request) {
        return ResponseEntity.status(CREATED).body(EntityModel.createMockModel(request.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel> updateEntity (@PathVariable UUID id, @RequestBody UpsertEntityRequest request) {
        return ResponseEntity.ok(new EntityModel(id, request.getName(), Instant.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel> deleteEntityById(@PathVariable UUID id) {
        log.info("Delete entity by ID {}", id);

        return ResponseEntity.noContent().build();
    }

}
