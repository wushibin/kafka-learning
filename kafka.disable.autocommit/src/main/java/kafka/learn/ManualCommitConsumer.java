package kafka.learn;

import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ManualCommitConsumer extends ShutdownableThread {

    private final String topic;
    private final String groupId;
    private final String clientId;
    private final KafkaConsumer<String, String> consumer;

    public ManualCommitConsumer(String connectionUrl, String clientId, String groupId, String topic, Properties additions) {
        super("ManualCommitConsumer_" + clientId, true);

        this.topic = topic;
        this.groupId = groupId;
        this.clientId = clientId;

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionUrl);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringDeserializer");

        if (Objects.nonNull(additions)){
            properties.putAll(additions);
        }

        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(this.topic));
    }

    @Override
    public void doWork() {
        ConsumerRecords<String, String> records = consumer.poll(1000);

        for (TopicPartition partition : records.partitions()){
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            for (ConsumerRecord<String, String> record : partitionRecords){
                log.info("Consumer " + this.clientId + " received message (" + record.key() + "," + record.value() + ")" +
                                 " from partition (" + record.partition() + ")");
                log.debug("Record information: " + record.toString());
            }

            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset() + 1;
            log.info("Commit the partition (" + partition.partition() + ") offset (" + lastOffset + ")");
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset)));
        }
    }
}
