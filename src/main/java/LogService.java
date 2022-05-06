import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        var logService =  new LogService();

        //Se uma exception for lançada ao criar o kafkaService, kafkaService.close é chamada
        //Se uma execption não for lanáda ao criar o kafkaSerive, kafkaService.close é chamada após kafkaService.run ser chamada
        try(var kafkaService = new KafkaService(LogService.class.getSimpleName(), Pattern.compile("ECOMMERCE.*"), logService::parse, String.class, Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))){
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, String> record) {

        System.out.println("--------------------------------------------");
        System.out.println("LOG: " + record.topic());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

    }

}
