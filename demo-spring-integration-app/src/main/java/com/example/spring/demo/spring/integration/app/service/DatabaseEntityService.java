package com.example.spring.demo.spring.integration.app.service;

import com.example.spring.demo.spring.integration.app.entity.DatabaseEntity;
import com.example.spring.demo.spring.integration.app.repository.DatabaseEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.spring.demo.spring.integration.app.configuration.properties.AppCacheProperties.CacheNames.DATABASE_ENTITIES;
import static com.example.spring.demo.spring.integration.app.configuration.properties.AppCacheProperties.CacheNames.DATABASE_ENTITY_BY_ID;
import static com.example.spring.demo.spring.integration.app.configuration.properties.AppCacheProperties.CacheNames.DATABASE_ENTITY_BY_NAME;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class DatabaseEntityService {

    private final DatabaseEntityRepository repository;

    @Cacheable(DATABASE_ENTITIES)
    public List<DatabaseEntity> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = DATABASE_ENTITY_BY_ID, key = "#id")
    public DatabaseEntity findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable(DATABASE_ENTITY_BY_NAME)
    public DatabaseEntity findByName(String name) {
        DatabaseEntity probe = new DatabaseEntity();
        probe.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "date");

        Example<DatabaseEntity> example = Example.of(probe, matcher);

        return repository.findOne(example).orElseThrow();
    }

    @CacheEvict(value = DATABASE_ENTITIES, allEntries = true)
    public DatabaseEntity create(DatabaseEntity entity) {
        DatabaseEntity forSave = new DatabaseEntity();
        forSave.setName(entity.getName());
        forSave.setDate(entity.getDate());

        return repository.save(forSave);
    }

    @Caching(evict = {
            @CacheEvict(value = DATABASE_ENTITIES, allEntries = true),
            @CacheEvict(value = DATABASE_ENTITY_BY_NAME, allEntries = true)
    })
//    @CacheEvict(cacheNames = DATABASE_ENTITY_BY_ID, key = "#id", beforeInvocation = true)
    public DatabaseEntity update(UUID id, DatabaseEntity entity) {
        DatabaseEntity forUpdate = findById(id);
        forUpdate.setName(entity.getName());
        forUpdate.setDate(entity.getDate());

        return repository.save(forUpdate);
    }

    @Caching(evict = {
            @CacheEvict(value = DATABASE_ENTITIES, allEntries = true),
            @CacheEvict(value = DATABASE_ENTITY_BY_NAME, allEntries = true)
    })
//    @CacheEvict(cacheNames = DATABASE_ENTITY_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
