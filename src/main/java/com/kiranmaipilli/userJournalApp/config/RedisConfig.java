package com.kiranmaipilli.userJournalApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        return factory;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory ){
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(factory); // setting up the connection
        template.setKeySerializer(new StringRedisSerializer()); // setting up the serializer for key value
        template.setValueSerializer(new StringRedisSerializer()); // setting up the serializer of value

        return template;
    }

}
