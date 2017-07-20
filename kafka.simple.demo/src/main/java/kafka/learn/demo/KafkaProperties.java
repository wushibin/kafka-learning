package kafka.learn.demo;

public class KafkaProperties {
    public static final String TOPIC = "SIMPLE_DEMO_TOPIC";
    public static final String KAFKA_CONNECTION_URL = "172.16.238.10:9092,172.16.238.20:9097,172.16.238.30:9093,"+
            "172.16.238.40:9094,172.16.238.50:9095,172.16.238.60:9096";
//    public static final String KAFKA_CONNECTION_URL = "localhost:9092";

    private KafkaProperties() {

    }
}
