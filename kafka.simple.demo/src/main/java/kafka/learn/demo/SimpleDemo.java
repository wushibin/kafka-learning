package kafka.learn.demo;

import kafka.learn.SimpleConsumer;
import kafka.learn.SimpleProducer;

public class SimpleDemo {
    public static void main(String[] args) throws Exception {
        SimpleProducer producer = new SimpleProducer(KafkaProperties.KAFKA_CONNECTION_URL, "SimpleProducer",
                                                     KafkaProperties.TOPIC, false, null);
        producer.start();

        SimpleConsumer consumer = new SimpleConsumer(KafkaProperties.KAFKA_CONNECTION_URL, "SimpleConsumer",
                                                     "SimpleConsumerGroup", KafkaProperties.TOPIC, null);
        consumer.start();

        producer.join();
    }
}
