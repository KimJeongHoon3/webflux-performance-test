package com.test.webfluxperformance.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.webfluxperformance.user.User;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class RedisRepository {
    private final ReactiveValueOperations<String, String> reactiveValueOperations;
    private final ObjectMapper objectMapper;
    private final String REDIS_KEY="performData";

    public RedisRepository(ReactiveStringRedisTemplate reactiveStringRedisTemplate, ObjectMapper objectMapper){
        reactiveValueOperations = reactiveStringRedisTemplate.opsForValue();
        this.objectMapper = objectMapper;
    }

    public Mono<Boolean> save(User user){
        return reactiveValueOperations.set(REDIS_KEY,getJsonStr(user));
    }

    private String getJsonStr(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public Mono<User> find() {
        return reactiveValueOperations.get(REDIS_KEY).map(this::getUser);
    }

    private User getUser(String jsonStr){
        try {
            return objectMapper.readValue(jsonStr,User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
