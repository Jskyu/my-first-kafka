package jsk.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafConsumerService {

    private final Consumer<String, String> consumer;

    @Value("${spring.kafka.consumer.topic}")
    private String DEFAULT_TOPIC;

    @KafkaListener(topics = "test-topic", groupId = "test")
    public void listenGroupTest(String message) {
        log.info("KafConsumerService.listenGroupTest() Consumed Message : {}", message);
    }

    public List<Object> getMessageByTopic(String ...topics) {
        if (topics.length == 0) {
            return Collections.emptyList();
        }

        List<String> topicList = List.of(topics);
        consumer.subscribe(topicList);
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));

        List<Object> list = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            if (topicList.contains(record.topic())) {
                list.add(record.value());
            }
        }
        log.info("KafConsumerService.getMessageByTopic() Consumed Message : {}", list.toString());

        return list;
    }
}