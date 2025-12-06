package com.example.expensetracker.config;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.CacheManager;

@Configuration
public class RateLimitingConfig {

    @Bean
    public CacheManager jcacheManager() {
        CaffeineCachingProvider caffeineCachingProvider = new CaffeineCachingProvider();
        CacheManager cacheManager = caffeineCachingProvider.getCacheManager(
                caffeineCachingProvider.getDefaultURI(),
                null
        );
        MutableConfiguration<String, Object> cacheConfig = new MutableConfiguration<>();
        cacheConfig.setStoreByValue(false);
        cacheManager.createCache("rate-limit-cache", cacheConfig);
        return cacheManager;
    }
}
