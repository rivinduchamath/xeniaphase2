package com.cloudofgoods.xenia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories({"com.cloudofgoods.xenia.repository"})
@EnableCaching(proxyTargetClass = true)
public class ApplicationMainXenia {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMainXenia.class, args);
    }
}
