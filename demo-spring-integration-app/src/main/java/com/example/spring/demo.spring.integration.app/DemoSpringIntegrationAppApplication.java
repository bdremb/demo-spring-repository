package com.example.spring.demo.spring.integration.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoSpringIntegrationAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringIntegrationAppApplication.class, args);
    }

}
