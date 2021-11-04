package com.test.webfluxperformance.router;

import com.test.webfluxperformance.user.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction userRouter(UserHandler userHandler){

        return nest(path("/users")
                ,nest(accept(MediaType.APPLICATION_JSON)
                        ,route().GET(userHandler::find)
                                .build())
                .andNest(contentType(MediaType.APPLICATION_JSON)
                        ,route().POST(userHandler::registerUser)
                                .build()));

    }

}
