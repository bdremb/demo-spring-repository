package com.example.spring.demo.spring.integration.app.service;

import com.example.spring.demo.spring.integration.app.entity.DatabaseEntity;
import com.example.spring.demo.spring.integration.app.repository.DatabaseEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatabaseEntityService {

    private final DatabaseEntityRepository repository;

    @Cacheable("databaseEntities")
    public List<DatabaseEntity> findAll() {
        return repository.findAll();
    }

    public DatabaseEntity findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable("databaseEntityByName")
    public DatabaseEntity findByName(String name) {
        DatabaseEntity probe = new DatabaseEntity();
        probe.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "date");

        Example<DatabaseEntity> example = Example.of(probe, matcher);

        return repository.findOne(example).orElseThrow();
    }

    @CacheEvict(value = "databaseEntities", allEntries = true)
    public DatabaseEntity create(DatabaseEntity entity) {
        DatabaseEntity forSave = new DatabaseEntity();
        forSave.setName(entity.getName());
        forSave.setDate(entity.getDate());

        return repository.save(forSave);
    }

    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityByName", allEntries = true)
    })
    public DatabaseEntity update(UUID id, DatabaseEntity entity) {
        DatabaseEntity forUpdate = findById(id);
        forUpdate.setName(entity.getName());
        forUpdate.setDate(entity.getDate());

        return repository.save(forUpdate);
    }

    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityByName", allEntries = true)
    })
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
