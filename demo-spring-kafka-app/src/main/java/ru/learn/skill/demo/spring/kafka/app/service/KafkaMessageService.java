package ru.learn.skill.demo.spring.kafka.app.service;

import org.springframework.stereotype.Service;
import ru.learn.skill.demo.spring.kafka.app.model.KafkaMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KafkaMessageService {

    private final List<KafkaMessage> messages = new ArrayList<>();

    public void add(KafkaMessage message) {
        messages.add(message);
    }

    public Optional<KafkaMessage> getById(Long id) {
        return messages.stream().filter(it -> it.getId().equals(id)).findFirst();
    }

}
