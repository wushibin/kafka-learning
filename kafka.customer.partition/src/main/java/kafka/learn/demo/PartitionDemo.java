package kafka.learn.demo;


import kafka.learn.RandomPartition;
import kafka.learn.SimpleConsumer;
import kafka.learn.SimpleProducer;

import java.util.Properties;

public class PartitionDemo {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("partitioner.class", "kafka.learn.RandomPartition");

        SimpleProducer producer = new SimpleProducer(KafkaProperties.KAFKA_CONNECTION_URL, "CustomerPartitionProducer",
                                                     KafkaProperties.TOPIC, false, properties);
        producer.start();

        SimpleConsumer consumer = new SimpleConsumer(KafkaProperties.KAFKA_CONNECTION_URL, "CustomerPartitionConsumer",
                                                     "PartitionConsumerGroup", KafkaProperties.TOPIC, null);
        consumer.start();
    }
}
