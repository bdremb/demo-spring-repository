package ru.server.example.demo.spring.app.client.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.server.example.demo.spring.app.client.web.model.EntityModel;

public interface EntityModelClient {

    @GetExchange
    Flux<EntityModel> getModels();

    @GetExchange("/{id}")
    Mono<EntityModel> getModel(@PathVariable Long id);

    @PostExchange
    Mono<EntityModel> createModel(@RequestBody EntityModel model);

    @PutExchange("/{id}")
    Mono<EntityModel> updateModel(@RequestBody EntityModel entityModel, @PathVariable Long id);

    @DeleteExchange("/{id}")
    Mono<ResponseEntity<Void>> deleteModel(@PathVariable Long id);

    @GetExchange("/exception")
    Mono<ResponseEntity<Void>> exceptionMethod();
}
