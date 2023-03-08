package jsk.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer<String, Object> producer;
    private final KafkaConsumer<String, Object> consumer;

    //토픽을 N개 사용할 시 로직 개선이 필요할듯 함.
    @Value("${spring.kafka.consumer.group-id}")
    private String topic;

    @PostMapping("/kafka/send")
    public String sendMessage(@RequestBody Map<String, Object> paramMap) {
        Object message = paramMap.getOrDefault("message", "");
        if("".equals(message)) {
            return "failed";
        } else {
            producer.send(new ProducerRecord<>(topic, message));
            return "success";
        }
    }

    @GetMapping("/get")
    public List<Object> getMessage(@RequestParam(name = "topic", required = true) String topic) {
        if(!this.topic.equals(topic)) { // 등록된 토픽이 아니라면 빈값 반환
            return Collections.emptyList();
        }

        List<Object> list = new ArrayList<>();

        consumer.subscribe(List.of(topic));
        ConsumerRecords<String, Object> records = consumer.poll(500);

        for (ConsumerRecord<String, Object> record : records) {
            if (topic.equals(record.topic())) {
                list.add(record.value());
            }
        }

        return list;
    }
}