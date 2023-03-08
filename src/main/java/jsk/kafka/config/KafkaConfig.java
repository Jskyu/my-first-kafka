package jsk.kafka.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String producerHost;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerHost;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerTopic;

    @Bean
    public <K, V> KafkaProducer<K, V> kafkaProducer(){
        Properties configs = new Properties();
        configs.put("bootstrap.servers", producerHost); // kafka host 및 server 설정
        configs.put("acks", "all");                         // 자신이 보낸 메시지에 대해 카프카로부터 확인을 기다리지 않습니다.
        configs.put("block.on.buffer.full", "true");        // 서버로 보낼 레코드를 버퍼링 할 때 사용할 수 있는 전체 메모리의 바이트수
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");   // serialize 설정
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // serialize 설정

        return new KafkaProducer<>(configs);
    }

    @Bean
    public <K, V> KafkaConsumer<K, V> kafkaConsumer() {
        Properties configs = new Properties();
        configs.put("bootstrap.servers", consumerHost); // kafka host 및 server 설정
        configs.put("session.timeout.ms", "10000");     // session 설정
        configs.put("group.id", consumerTopic);   // topic 설정
        configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");    // key deserializer
        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  // value deserializer

        return new KafkaConsumer<>(configs);
    }
}
