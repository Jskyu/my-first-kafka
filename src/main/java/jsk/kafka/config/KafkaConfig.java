package jsk.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String producerHost;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerHost;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean(name = "kafkaProducer")
    public KafkaProducer<String, Object> kafkaProducer(){
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerHost); // kafka host 및 server 설정
        configs.put(ProducerConfig.ACKS_CONFIG, "all");                         // 자신이 보낸 메시지에 대해 카프카로부터 확인을 기다리지 않습니다.
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);   // serialize 설정
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize 설정

        return new KafkaProducer<>(configs);
    }

    @Bean(name = "kafkaConsumer")
    public KafkaConsumer<String, Object> kafkaConsumer() {
        return new KafkaConsumer<>(consumerProperties());
    }

    private Map<String, Object> consumerProperties(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerHost); // kafka host 및 server 설정
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");     // session 설정
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);   // topic 설정
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);    // key deserializer
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);  // value deserializer
        return configs;
    }

    /* message listener */
    //FIXME DO NOT WORKING
    @Bean(name = "kafkaMessageListenerContainer")
    public KafkaMessageListenerContainer<String, Object> kafkaMessageListenerContainer() {

        ContainerProperties containerProperties = new ContainerProperties("test");
        containerProperties.setMessageListener((MessageListener<String, Object>) (data) -> {
            log.info("Listener Consumed Message : " + data.value());
        });

        ConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties());
        KafkaMessageListenerContainer<String, Object> listenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setAutoStartup(false);
        listenerContainer.setBeanName("kafka-message-listener");

        return listenerContainer;
    }
}
