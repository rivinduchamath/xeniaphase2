package com.cloudofgoods.xenia.config.droolconfig.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.*;

@Configuration
@Slf4j
public class CachingConfig {
    @Value("${cloudOfGoods.redis.timeout.organization}")
    private Long organization;
    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        log.info("Log::CachingConfig redisCacheManagerBuilderCustomizer()");
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap
                    .put("organization", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(organization))
                            .disableCachingNullValues());
            configurationMap
                    .put("organization-data", RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofMinutes(organization))
                            .disableCachingNullValues());
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}