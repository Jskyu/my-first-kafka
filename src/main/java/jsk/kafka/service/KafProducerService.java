package jsk.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafProducerService {

    @Value("${spring.kafka.consumer.topic}")
    private String DEFAULT_TOPIC;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object message, @Nullable String topic) {
        if (topic == null) {
            topic = DEFAULT_TOPIC;
        }
        this.kafkaTemplate.send(topic, message);
        log.info("KAFKA Send message topic: {}, message: {}", topic, message);
    }
}
