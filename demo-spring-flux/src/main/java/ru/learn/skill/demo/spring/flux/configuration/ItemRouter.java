package ru.learn.skill.demo.spring.flux.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.learn.skill.demo.spring.flux.handler.ItemHandler;

@Configuration
public class ItemRouter {

    @Bean
    public RouterFunction<ServerResponse> itemRouters(ItemHandler itemHandler) {
        return RouterFunctions.route()
                .GET("/api/v1/functions/item", itemHandler::getAllItems)
                .GET("/api/v1/functions/item/{id}", itemHandler::findById)
                .POST("/api/v1/functions/item", itemHandler::createItem)
                .GET("/api/v1/functions/error", itemHandler::errorRequest)
                .build();
    }

}
