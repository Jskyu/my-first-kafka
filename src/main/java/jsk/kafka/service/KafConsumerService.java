package jsk.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KafConsumerService {

    private final KafkaConsumer<String, Object> kafkaConsumer;

    @Qualifier
    public final KafkaMessageListenerContainer<String, Object> kafkaMessageListenerContainer;

    @Autowired
    public KafConsumerService(KafkaConsumer<String, Object> kafkaConsumer, KafkaMessageListenerContainer<String, Object> kafkaMessageListenerContainer) {
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaMessageListenerContainer = kafkaMessageListenerContainer;
        this.kafkaMessageListenerContainer.start();
    }

    @Value("${spring.kafka.consumer.topic}")
    private String DEFAULT_TOPIC;

    public List<Object> getMessageByTopic(String ...topics) {
        if (topics.length == 0) {
            return Collections.emptyList();
        }

        List<String> topicList = List.of(topics);
        kafkaConsumer.subscribe(topicList);
        ConsumerRecords<String, Object> records = kafkaConsumer.poll(500);

        List<Object> list = new ArrayList<>();

        for (ConsumerRecord<String, Object> record : records) {
            if (topicList.contains(record.topic())) {
                list.add(record.value());
            }
        }
        log.info("KafConsumerService.getMessageByTopic() Consumed Message : {}", list.toString());

        return list;
    }
}