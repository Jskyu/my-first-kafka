package jsk.kafka.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class KafConsumerServiceTest {

    private final String TOPIC = "test-topic";

    private final KafConsumerService kafConsumerService;

    @Autowired
    public KafConsumerServiceTest(KafConsumerService kafConsumerService) {
        this.kafConsumerService = kafConsumerService;
    }

    @Test
    public void getMessageByTopicTest(){
        //Given
        String topic = TOPIC;
        List<Object> list = Collections.emptyList();
        //When
        try {
            list = kafConsumerService.getMessageByTopic(topic);
        } catch (Exception e) {
            Assertions.fail();
        }

        //Then
        //오류가 나면 안됌
        System.out.println(list.toString());
    }
}
