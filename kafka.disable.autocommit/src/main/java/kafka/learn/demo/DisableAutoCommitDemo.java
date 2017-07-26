package kafka.learn.demo;

import kafka.learn.ManualCommitConsumer;
import kafka.learn.SimpleProducer;

public class DisableAutoCommitDemo {

    public static void main(String[] args) throws Exception {

        SimpleProducer producer = new SimpleProducer(KafkaProperties.KAFKA_CONNECTION_URL, "DisableAutoCommitProducer",
                                                     KafkaProperties.TOPIC, false, null);
        producer.start();

        ManualCommitConsumer consumer = new ManualCommitConsumer(KafkaProperties.KAFKA_CONNECTION_URL,
                                                                 "DisableAutoCommitConsumer",
                                                                 "DisableAutoCommitConsumerGroup",
                                                                 KafkaProperties.TOPIC, null);
        consumer.start();
    }
}
