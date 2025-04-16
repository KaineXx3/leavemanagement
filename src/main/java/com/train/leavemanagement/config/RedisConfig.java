package com.train.leavemanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration // Marks this class as a configuration class for Spring
@EnableCaching // Enables Spring's annotation-driven caching support (e.g., @Cacheable)
public class RedisConfig {

    // Configure a Redis-based CacheManager for use with Spring's caching annotations
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        // Set default cache configuration with TTL of 10 minutes
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Time-to-live for cache entries
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer())) // Use String for keys
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper) // Use Jackson JSON for values
                ));

        // Build and return a RedisCacheManager with the above settings
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

    // Create a RedisTemplate bean for manual Redis operations (e.g., token storage)
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory); // Connect to Redis server

        // Use plain strings for both keys and values
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template; // Return the configured RedisTemplate
    }

}
