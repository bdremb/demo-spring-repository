package ru.learn.skill.demo.spring.kafka.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.learn.skill.demo.spring.kafka.app.model.KafkaMessage;
import ru.learn.skill.demo.spring.kafka.app.service.KafkaMessageService;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class KafkaMessageListenerTest {

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
    );


    @DynamicPropertySource
    static void registryKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void whenSendKafkaMessage_thenHandleMessageByListener() {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setId(3L);
        kafkaMessage.setMessage("Some kafka message");
        String key = UUID.randomUUID().toString();

        kafkaTemplate.send(topicName, key, kafkaMessage);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<KafkaMessage> mayBeKafkaMessage = kafkaMessageService.getById(3L);

                    assertThat(mayBeKafkaMessage).isPresent();

                    KafkaMessage receivedKafkaMessage = mayBeKafkaMessage.get();
                    assertThat(receivedKafkaMessage.getMessage()).isEqualTo("Some kafka message");
                    assertThat(receivedKafkaMessage.getId()).isEqualTo(3L);
                });
    }

}
