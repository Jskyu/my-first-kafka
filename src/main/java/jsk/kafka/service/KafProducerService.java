package jsk.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafProducerService {

    @Value("${spring.kafka.consumer.topic}")
    private String DEFAULT_TOPIC;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProducer<String, String> kafkaProducer;

    public ListenableFuture<SendResult<String, String>> sendMessage1(String message, @Nullable String topic) {
        topic = checkNull(topic);
        ListenableFuture<SendResult<String, String>> send = this.kafkaTemplate.send(topic, message);

        send.addCallback(CustomListenableFutureCallback.instance);

        return send;
    }

    public ListenableFuture<SendResult<String, String>> sendMessage2(String message, @Nullable String topic) {
        topic = checkNull(topic);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(record);

        send.addCallback(CustomListenableFutureCallback.instance);

        return send;
    }

    public Future<RecordMetadata> sendMessage3(String message, @Nullable String topic) {
        topic = checkNull(topic);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        Future<RecordMetadata> send = kafkaProducer.send(record);
        kafkaProducer.close();

        return send;
    }

    private String checkNull(String topic) {
        if (topic == null) {
            topic = DEFAULT_TOPIC;
        }
        return topic;
    }

    private static class CustomListenableFutureCallback implements ListenableFutureCallback<SendResult<String, String>> {

        private static final CustomListenableFutureCallback instance = new CustomListenableFutureCallback();

        @Override
        public void onFailure(Throwable ex) {
            //Failed
            log.error("ERROR KAFKA SEND MESSAGE", ex);
        }

        @Override
        public void onSuccess(SendResult<String, String> result) {
            //Success
            ProducerRecord<String, String> record = result.getProducerRecord();
            String topic = record.topic();
            Object value = record.value();
            log.info("Success KAFKA Send message topic: {}, value: {}", topic, value);
        }
    }
}
