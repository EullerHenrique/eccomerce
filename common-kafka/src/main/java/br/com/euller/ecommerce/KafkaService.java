package br.com.euller.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    private KafkaService(String groupId, ConsumerFunction parse, Class<T> type, Map<String, String> pDeserializer) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(type, groupId, pDeserializer));
    }

    public KafkaService(String groupId, String topic,  ConsumerFunction parse, Class<T> type, Map<String, String> pDeserializer) {

        this(groupId, parse, type, pDeserializer);
        consumer.subscribe(Collections.singletonList(topic));

    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> pDeserializer) {

        this(groupId, parse, type, pDeserializer);
        consumer.subscribe(topic);

    }


    private Properties properties(Class<T> type, String groupId, Map<String, String> pDeserializer) {

        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());

        //Faz com que o pool consuma somente um record por loop, com isso, o rebalancing
        //(rebalancing -> Distribui novamente os records pelas partições)
        //não impede que os commits (commit-> Notifica que a mensagem foi consumida) sejam feitos corretamente, pois
        //agora se o rebalancing for feito, não háverá records que não foram consumidos na partição incorreta
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        properties.putAll(pDeserializer);

        return properties;

    }

    public void run() {

        while(true){
            var records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Encontrei  " + records.count() + " registros");
                for(var record: records) {
                    parse.consume(record);
                }
            }

        }

    }

    @Override
    public void close()  {
        consumer.close();
    }
}
