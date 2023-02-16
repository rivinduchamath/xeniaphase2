package com.cloudofgoods.xenia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication
@EnableMongoAuditing
@ConfigurationPropertiesScan
@EnableMongoRepositories({"com.cloudofgoods.xenia.repository"})
@EnableCaching(proxyTargetClass=true)
@EnableRedisRepositories
public class ApplicationMainXenia {
    public static void main(String[] args) {
        SpringApplication.run (ApplicationMainXenia.class, args);
    }
}
