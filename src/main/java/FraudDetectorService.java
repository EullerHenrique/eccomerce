import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;


public class FraudDetectorService {
    public static void main(String[] args) {

        var fraudService = new FraudDetectorService();
        //Se uma exception for lançada ao criar o kafkaService, kafkaService.close é chamada
        //Se uma execption não for lanáda ao criar o kafkaSerive, kafkaService.close é chamada após kafkaService.run ser chamada
        try(var kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", fraudService::parse, Order.class, Map.of())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("--------------------------------------------");
        System.out.println("Processando novo pedido, buscando uma fraude");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ordem processada");
    }


}
