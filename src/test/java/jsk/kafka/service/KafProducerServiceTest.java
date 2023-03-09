package jsk.kafka.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.KafkaException;

@SpringBootTest
public class KafProducerServiceTest {

    private final String TOPIC = "test-topic";

    private final KafProducerService kafProducerService;

    @Autowired
    public KafProducerServiceTest(KafProducerService kafProducerService) {
        this.kafProducerService = kafProducerService;
    }

    @Test
    public void sendMessageTest(){
        //Given
        String message = "Hello Test Message";

        try {
            //When
            kafProducerService.sendMessage(message, null);
            kafProducerService.sendMessage(message, TOPIC);
        } catch (KafkaException e) {
            Assertions.fail();
        }

        //Then
        //오류가 나면 안됌
    }
}
