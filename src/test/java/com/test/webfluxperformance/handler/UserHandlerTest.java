package com.test.webfluxperformance.handler;

import com.test.webfluxperformance.router.RouterConfig;
import com.test.webfluxperformance.user.User;
import com.test.webfluxperformance.user.UserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

//@WebFluxTest
//@Import({UserHandler.class, RouterConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserHandlerTest {
    @Autowired
    WebTestClient webTestClient;

    final User user = User.builder().id("id-123")
            .hobby("basketball")
            .name("kim")
            .build();

    @Test
    void test_register(){
        webTestClient.post().uri("/users")
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println);

    }

    @Test
    void test_find(){
        test_register();

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user)
                .consumeWith(System.out::println);
    }
}