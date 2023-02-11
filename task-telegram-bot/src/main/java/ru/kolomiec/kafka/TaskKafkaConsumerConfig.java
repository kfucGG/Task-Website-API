package ru.kolomiec.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import ru.kolomiec.dto.TaskDTO;
import java.util.Arrays;
import java.util.Properties;

public class TaskKafkaConsumerConfig {

    private Properties getConsumerProperties() {
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        kafkaConsumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "telegram.bot");
        kafkaConsumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConsumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return kafkaConsumerProperties;
    }
    private String getBootstrapServer() {
        if (System.getenv("FROM_DOCKER") != null) {
            return "kafka:9092";
        }
        return "localhost:9092";
    }
    public KafkaConsumer<String, String> getTaskKafkaConsumer() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConsumerProperties());
        consumer.subscribe(Arrays.asList("task"));
        return consumer;
    }

}
