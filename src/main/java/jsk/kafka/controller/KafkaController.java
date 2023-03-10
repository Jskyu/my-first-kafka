package jsk.kafka.controller;

import jsk.kafka.service.KafConsumerService;
import jsk.kafka.service.KafProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafProducerService producerService;
    private final KafConsumerService consumerService;

    //토픽을 N개 사용할 시 로직 개선이 필요할듯 함.
    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Map<String, String> paramMap) {
        String message = paramMap.getOrDefault("message", "");
        if("".equals(message)) {
            return "failed";
        } else {
            producerService.sendMessage1(message, topic);
            return "success";
        }
    }

    @GetMapping("/get")
    public List<String> getMessage(@RequestParam(name = "topic", required = true) String topic) {
        List<String> list = consumerService.getMessageByTopic(topic);

        return list;
    }
}
