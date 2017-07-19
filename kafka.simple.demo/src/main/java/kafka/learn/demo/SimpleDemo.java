package kafka.learn.demo;

public class SimpleDemo {
    public static void main(String [] args){
        SimpleProducer producer = new SimpleProducer("SimpleProducer", KafkaProperties.TOPIC, true);
        producer.start();

        SimpleConsumer consumer = new SimpleConsumer("SimpleConsumer", "SimpleConsumerGroup", KafkaProperties.TOPIC);
        consumer.start();
    }
}
