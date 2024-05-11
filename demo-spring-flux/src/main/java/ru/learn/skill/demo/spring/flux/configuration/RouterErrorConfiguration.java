package ru.learn.skill.demo.spring.flux.configuration;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Configuration
public class RouterErrorConfiguration {

    //Disable for testing
//    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest serverRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(serverRequest, options);
                errorAttributes.put("error-attribute", "Error in request!");
                errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());

                return errorAttributes;
            }

        };
    }

}
