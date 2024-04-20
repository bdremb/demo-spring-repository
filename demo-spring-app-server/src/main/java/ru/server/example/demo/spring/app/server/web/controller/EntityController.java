package ru.server.example.demo.spring.app.server.web.controller;

import lombok.extern.slf4j.Slf4j;
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
import ru.server.example.demo.spring.app.server.web.model.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/v1/model")
@Slf4j
public class EntityController {

    @GetMapping
    public ResponseEntity<List<EntityModel>> getAll() {
        List<EntityModel> models = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            models.add(EntityModel.builder()
                    .id(ThreadLocalRandom.current().nextLong())
                    .name("Entity Model " + i)
                    .age(20 + i)
                    .models(List.of(
                            EntityModel.builder()
                                    .id(ThreadLocalRandom.current().nextLong())
                                    .name("Sub model " + i)
                                    .age(10 + i)
                                    .build()
                    ))
                    .build());
        }
        return ResponseEntity.ok(models);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                EntityModel.builder()
                        .id(ThreadLocalRandom.current().nextLong())
                        .name("Entity model " + id)
                        .age(20)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel> createModel(@RequestBody EntityModel model) {
        log.info("Create model {}", model);
        model.setId(ThreadLocalRandom.current().nextLong());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel> updateModel(@RequestBody EntityModel model, @PathVariable Long id) {
        log.info("Update model {} with id {}", model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.info("Delete model with id {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exception")
    public ResponseEntity<Void> exceptionMethod() {
        throw new RuntimeException("Exception at runtime");
    }

}
