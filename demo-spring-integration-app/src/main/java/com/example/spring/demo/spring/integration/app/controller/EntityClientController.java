package com.example.spring.demo.spring.integration.app.controller;


import com.example.spring.demo.spring.integration.app.clients.OpenFeignClient;
import com.example.spring.demo.spring.integration.app.model.EntityModel;
import com.example.spring.demo.spring.integration.app.model.UpsertEntityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/entity")
@RequiredArgsConstructor
public class EntityClientController {

//    private final OkHttpClientSender clientSender;

//    private final RestTemplateClient clientSender;

//    private final WebClientSender clientSender;

    private final OpenFeignClient clientSender;

    @GetMapping
    public ResponseEntity<List<EntityModel>> entityList() {
        return ResponseEntity.ok(clientSender.getEntityList());
    }

    @GetMapping("/{name}")
    public ResponseEntity<EntityModel> entityByName(@PathVariable String name) {
        return ResponseEntity.ok(clientSender.getEntityByName(name));
    }

    @PostMapping
    public ResponseEntity<EntityModel> createEntity(@RequestBody UpsertEntityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientSender.createEntity(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel> updateEntity(@PathVariable UUID id, @RequestBody UpsertEntityRequest request) {
        return ResponseEntity.ok(clientSender.updateEntity(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel> deleteEntityById(@PathVariable UUID id) {
        clientSender.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }


}
