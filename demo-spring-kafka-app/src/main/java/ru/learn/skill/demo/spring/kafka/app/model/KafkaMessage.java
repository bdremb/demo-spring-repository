package ru.learn.skill.demo.spring.kafka.app.model;

import lombok.Data;

@Data
public class KafkaMessage {

    private Long id;

    private String message;
}
