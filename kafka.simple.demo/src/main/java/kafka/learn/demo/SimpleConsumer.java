package kafka.learn.demo;

import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

@Slf4j
public class SimpleConsumer extends ShutdownableThread {
    private String topic;
    private String groupId;
    private String clientId;
    private KafkaConsumer<String, String> consumer;

    public SimpleConsumer(String clientId, String groupId, String topic) {
        super("SimpleConsumer_" + clientId, true);
        this.topic = topic;
        this.groupId = groupId;
        this.clientId = clientId;

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_CONNECTION_URL);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(this.topic));
    }

    @Override
    public void doWork() {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            log.info("Received message (" + record.key() + "," + record.value() + ")");
            log.info("Record infomation: " + record.toString());
        }
    }
}
