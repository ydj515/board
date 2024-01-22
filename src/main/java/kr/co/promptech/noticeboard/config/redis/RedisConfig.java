package kr.co.promptech.noticeboard.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

//    @Value("${spring.data.redis.sentinel.master}")
//    private String sentinelMaster;
//
//    @Value("${spring.data.redis.sentinel.nodes}")
//    private String sentinelNodes;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(sentinelMaster, parseSentinelNodes(sentinelNodes));
//        return new LettuceConnectionFactory(sentinelConfig);
//    }
//
//    private Set<String> parseSentinelNodes(String nodes) {
//        return new HashSet<>(Arrays.asList(nodes.split(",")));
//    }

}
