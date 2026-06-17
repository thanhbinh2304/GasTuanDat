package com.example.GasTuanDat.common.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        cacheManager.setCaches(Arrays.asList(
            buildCache("products", 1, TimeUnit.HOURS, 500),
            buildCache("purchaseOrders", 30, TimeUnit.MINUTES, 500),
            buildCache("saleInvoices", 30, TimeUnit.MINUTES, 500),
            buildCache("reports", 30, TimeUnit.MINUTES, 100)
        ));

        return cacheManager;
    }

    private CaffeineCache buildCache(String name, long duration, TimeUnit timeUnit, long maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
            .expireAfterWrite(duration, timeUnit)
            .maximumSize(maxSize)
            .build());
    }
}
