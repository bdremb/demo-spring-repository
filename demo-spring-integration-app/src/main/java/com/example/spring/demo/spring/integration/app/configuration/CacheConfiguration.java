package com.example.spring.demo.spring.integration.app.configuration;

import com.example.spring.demo.spring.integration.app.configuration.properties.AppCacheProperties;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnExpression("'${app.cache.cache-type}'.equals('in_memory')")
    public ConcurrentMapCacheManager inMemoryCacheManager(AppCacheProperties appCacheProperties) {
        var cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(appCacheProperties.getCaches().get(name).getExpiry())
                                .build()
                                .asMap(),
                        true
                );
            }
        };

        var cacheNames = appCacheProperties.getCacheNames();

        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }

        return cacheManager;
    }

}
