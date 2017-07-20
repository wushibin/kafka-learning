package kafka.learn.demo;

public class SimpleDemo {
    public static void main(String [] args) throws Exception{
        SimpleProducer producer = new SimpleProducer("SimpleProducer", KafkaProperties.TOPIC, false);
        producer.start();

        SimpleConsumer consumer = new SimpleConsumer("SimpleConsumer", "SimpleConsumerGroup", KafkaProperties.TOPIC);
        consumer.start();

        producer.join();
    }
}
