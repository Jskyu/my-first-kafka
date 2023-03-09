package jsk.kafka.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@SpringBootTest
public class KafProducerServiceTest {

    private final String TOPIC = "test-topic";

    private final KafProducerService kafProducerService;

    @Autowired
    public KafProducerServiceTest(KafProducerService kafProducerService) {
        this.kafProducerService = kafProducerService;
    }

    @Test
    public void sendMessage1Test(){
        //Given
        String message = "Hello Test Message1";

        //When
        ListenableFuture<SendResult<String, String>> sendResultListenableFuture1 = kafProducerService.sendMessage1(message, null);
        ListenableFuture<SendResult<String, String>> sendResultListenableFuture2 = kafProducerService.sendMessage1(message, TOPIC);

        boolean isDone1 = sendResultListenableFuture1.isDone();
        boolean isDone2 = sendResultListenableFuture2.isDone();

        //Then
        //FIXME 완료상태 ???
        Assertions.assertTrue(isDone1);
        Assertions.assertTrue(isDone2);
    }

    @Test
    public void sendMessage2Test(){
        //Given
        String message = "Hello Test Message2";

        //When
        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafProducerService.sendMessage2(message, null);
        boolean isDone = sendResultListenableFuture.completable().isDone();

        //Then
        //FIXME 완료상태 ???
        Assertions.assertTrue(isDone);
    }

    @Test
    public void sendMessage3Test(){
        //Given
        String message = "Hello Test Message3";

        //When
        Future<RecordMetadata> recordMetadataFuture = kafProducerService.sendMessage3(message, null);
        boolean isDone = recordMetadataFuture.isDone();

        //Then
        Assertions.assertTrue(isDone);
    }
}
