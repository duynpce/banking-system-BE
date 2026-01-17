package com.example.banking_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

@Component
public class Cleaner {
    @Autowired
    RedisCacheManager redisCacheManager;

    public void clearAllCaches() {
        redisCacheManager.getCacheNames().forEach(name -> {
            var cache = redisCacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
