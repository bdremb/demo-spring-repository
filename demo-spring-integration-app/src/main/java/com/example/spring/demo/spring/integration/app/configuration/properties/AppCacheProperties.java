package com.example.spring.demo.spring.integration.app.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> caches = new HashMap<>();

    private CacheType cacheType;

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String DATABASE_ENTITIES = "databaseEntities";
        String DATABASE_ENTITY_BY_NAME = "databaseEntityByName";
        String DATABASE_ENTITY_BY_ID = "databaseEntityById";
    }

    public enum CacheType {
        IN_MEMORY,
        REDIS
    }

}
