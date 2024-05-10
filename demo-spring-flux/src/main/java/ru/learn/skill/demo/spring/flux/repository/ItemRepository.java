package ru.learn.skill.demo.spring.flux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.learn.skill.demo.spring.flux.entity.Item;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findItemByName(String name);
}
