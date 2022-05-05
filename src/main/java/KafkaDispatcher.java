import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    public KafkaDispatcher() throws ExecutionException, InterruptedException {
        this.producer = new KafkaProducer<>(properties());

        for (var i = 0; i < 100; i++) {

            Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                System.out.println("SUCESSO...ENVIANDO -> " + " topic: " + data.topic() + " ::: " + "partition: " + data.partition() + " - " + "offset: " + data.offset() + " - " + "timestamp: " + data.timestamp());
            };

            var key = UUID.randomUUID().toString();
            var order = key + "PS5";
            var orderRecord = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, order);
            producer.send(orderRecord, callback).get();

            var email = "eullerhenrique@ufu.br";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);
            producer.send(emailRecord, callback).get();

        }
    }

    private Properties properties() {

        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;

    }

    public void send (String topic, String key, String value) throws ExecutionException, InterruptedException {

        var record = new ProducerRecord<>("topic", key, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("SUCESSO...ENVIANDO -> " + " topic: " + data.topic() + " ::: " + "partition: " + data.partition() + " - " + "offset: " + data.offset() + " - " + "timestamp: " + data.timestamp());
        };
        producer.send(record, callback).get();

    }

    @Override
    public void close() {
        producer.close();
    }
}
