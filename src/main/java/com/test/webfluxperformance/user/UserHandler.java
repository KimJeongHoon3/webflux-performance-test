package com.test.webfluxperformance.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.webfluxperformance.redis.RedisRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
public class UserHandler {
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    private WebClient webClient=WebClient.builder().build();

    public UserHandler(RedisRepository redisRepository,ObjectMapper objectMapper){

        this.redisRepository = redisRepository;
        this.objectMapper = objectMapper;
    }

    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(redisRepository::save)
                .flatMap(b -> {
                    if(b){
                        return ServerResponse.ok().build();
                    }else{
                        return Mono.error(new RuntimeException("redis save 에러"));
                    }
                });
    }

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        Mono<String> bodyToMono = webClient.get().uri("http://172.16.0.25:8046/test/api")
                .retrieve()
                .bodyToMono(String.class);

        Mono<User> userMono = redisRepository.find();

        Mono<User> zip = Mono.zip(bodyToMono, userMono, (s, user) -> {
            user.setHobby(s);
            return user;
        });

        return ServerResponse.ok().body(zip,User.class);
//        return redisRepository.find().flatMap(str -> ServerResponse.ok().bodyValue(getUser(str)));
    }

    private User getUser(String jsonStr){
        try {
            return objectMapper.readValue(jsonStr,User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
