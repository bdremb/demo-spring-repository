package ru.server.example.demo.spring.app.client.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.server.example.demo.spring.app.client.client.EntityModelClient;
import ru.server.example.demo.spring.app.client.web.model.EntityModel;

@RestController
@RequestMapping("/api/v1/client/model")
@RequiredArgsConstructor
public class EntityController {

    private final EntityModelClient client;

    @GetMapping
    public Flux<EntityModel> getAll() {
        return client.getModels();
    }

    @GetMapping("/{id}")
    public Mono<EntityModel> getById(@PathVariable Long id) {
        return client.getModel(id);
    }

    @PostMapping
    public Mono<EntityModel> createModel(@RequestBody EntityModel model) {
        return client.createModel(model);
    }

    @PutMapping("/{id}")
    public Mono<EntityModel> updateModel(@RequestBody EntityModel model, @PathVariable Long id) {
        return client.updateModel(model, id);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteModel(@PathVariable Long id) {
        return client.deleteModel(id);
    }

    @GetMapping("/exception")
    public Mono<ResponseEntity<Void>> exceptionMethod() {
        return client.exceptionMethod();
    }

}
