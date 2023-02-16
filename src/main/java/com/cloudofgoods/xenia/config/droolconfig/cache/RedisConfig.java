package com.cloudofgoods.xenia.config.droolconfig.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {
    @Value("${cloudOfGoods.redis.host}")
    private String redisHost;
    @Value("${cloudOfGoods.redis.port}")
    private int port;
    @Value("${cloudOfGoods.redis.password}")
    private String password;

    @Bean
    RedisTemplate <String, Object> RedisTemplate() {
        log.info ("Log::RedisConfig RedisTemplate()");
        RedisTemplate <String, Object> template = new RedisTemplate <> ();
        template.setConnectionFactory (jedisConnectionFactory ());
        template.setKeySerializer (new StringRedisSerializer ());
        template.setHashKeySerializer (new StringRedisSerializer ());
        template.setHashKeySerializer (new JdkSerializationRedisSerializer ());
        template.setValueSerializer (new JdkSerializationRedisSerializer ());
//        template.setEnableTransactionSupport(true);
        /*
           if You Use This template.setEnableTransactionSupport(true);
           The application was running fine but after some time, no connections could be acquired from the pool anymore.

           Without this template.setEnableTransactionSupport(true);
        * If you remove the line template.setEnableTransactionSupport(true), you will disable transaction support for the Redis template.
        * This means that you will not be able to use Redis transactions (i.e. MULTI / EXEC) with this template.
        * If you don't require transaction support in your application, removing this line should not cause any issues.

        * Enabling transaction support can result in connections not being released and lead to a shortage of connections in the pool over time.
        * This can cause the application to hang and not be able to acquire new connections from the pool,
        * leading to performance degradation or even complete failure.
        * */
//        template.setValueSerializer(new GenericToStringSerializer <Object> (Object.class));
        template.afterPropertiesSet ();
        return template;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = null;
        try {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration (redisHost,
                    port);
            if (!password.trim ().equals ("") && !redisHost.trim ().equals ("localhost")) {
                redisStandaloneConfiguration.setPassword (password);
            }
            jedisConnectionFactory = new JedisConnectionFactory (redisStandaloneConfiguration);
        } catch (RedisConnectionFailureException e) {
            log.error ("Connection break with redis " + e.getMessage ());
        }
        return jedisConnectionFactory;
    }
}