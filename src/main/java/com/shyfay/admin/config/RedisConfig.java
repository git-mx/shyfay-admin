package com.shyfay.admin.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by chenqixuan on 17/10/25.
 * 集成RedisTemplate
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {


    @Bean
    @ConfigurationProperties(prefix = "redis.pool")
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public JedisConnectionFactory getConnectionFactory() {
        JedisPoolConfig config = getRedisConfig();
        JedisConnectionFactory factory = new JedisConnectionFactory(config);
        return factory;
    }


    @Bean
    public RedisTemplate<?, ?> getRedisTemplate() {
        JedisConnectionFactory factory = getConnectionFactory();
        RedisTemplate<?, ?> template = new StringRedisTemplate(factory);
        return template;
    }
}
