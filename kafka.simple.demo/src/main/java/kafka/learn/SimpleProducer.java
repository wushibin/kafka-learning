package kafka.learn;

import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SimpleProducer extends ShutdownableThread {

    private String topic;
    private boolean isAsync;
    private KafkaProducer<String, String> producer;
    private long messageIndex = 0;

    public SimpleProducer(String connectionUrl, String clientId, String topic, boolean isAsync, Properties additions) {
        super("SimpleProducer_" + clientId, true);
        this.topic = topic;
        this.isAsync = isAsync;

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionUrl);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization.StringSerializer");

        if (Objects.nonNull(additions)) {
            properties.putAll(additions);
        }


        producer = new KafkaProducer<String, String>(properties);
        log.info("SimpleProducer starting....");
    }

    public void doWork() {
        String key = "key:" + messageIndex;
        String message = "msg:" + messageIndex;
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, message);
        if (isAsync) {
            AsyncCallback callback = new AsyncCallback(key, message);
            producer.send(record, callback);
        } else {
            try {
                producer.send(record).get();
                log.info("Send the message (" + key + "," + message + ")");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        messageIndex++;
    }

    class AsyncCallback implements Callback {
        private final String key;
        private final String message;

        public AsyncCallback(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (recordMetadata != null) {
                log.info("Message (" + key + "," + message + ") send to partition (" +
                                 recordMetadata.partition() + "), offset (" +
                                 +recordMetadata.offset() + ")");
            } else {
                e.printStackTrace();
            }
        }
    }
}
