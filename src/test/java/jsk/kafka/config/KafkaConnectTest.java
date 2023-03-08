package jsk.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Properties;

@SpringBootTest
public class KafkaConnectTest {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerHost;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String producerHost;

    private final String TOPIC = "test-topic";

    @Test
    public void producerTest() {
        //given
        Properties configs = getProducerProperties();

        try {
            // producer 생성
            KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

            //when
            // message 전달
            producer.send(new ProducerRecord<>(TOPIC, "Hello!!!"));

            //then
            producer.flush();
            producer.close();
        } catch (Exception e) {
            System.out.println("fail = " + e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    public void consumerTest(){
        //given
        Properties configs = getConsumerProperties();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);    // consumer 생성
        consumer.subscribe(List.of(TOPIC)); // topic 설정

        boolean isSuccess = false;
        while (!isSuccess) {
            //when
            ConsumerRecords<String, String> records = consumer.poll(500);
            for (ConsumerRecord<String, String> record : records) {
                String input = record.topic();
                //then

                if (TOPIC.equals(input)) {
                    System.out.println(record.value());
                    isSuccess = true;
                } else {
                    Assertions.fail();
                }
            }
        }
    }

    private Properties getProducerProperties() {
        Properties configs = new Properties();
        configs.put("bootstrap.servers", producerHost); // kafka host 및 server 설정
        configs.put("acks", "all");                     // 자신이 보낸 메시지에 대해 카프카로부터 확인을 기다리지 않습니다.
        configs.put("block.on.buffer.full", "true");    // 서버로 보낼 레코드를 버퍼링 할 때 사용할 수 있는 전체 메모리의 바이트수
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");   // serialize 설정
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // serialize 설정

        return configs;
    }

    private Properties getConsumerProperties() {
        Properties configs = new Properties();
        configs.put("bootstrap.servers", consumerHost); // kafka host 및 server 설정
        configs.put("session.timeout.ms", "10000");     // session 설정
        configs.put("group.id", TOPIC);   // topic 설정
        configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");    // key deserializer
        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return configs;
    }
}