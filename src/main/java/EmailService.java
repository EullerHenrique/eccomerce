import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {

        public static void main(String[] args) {

            var emailService = new EmailService();

            //Se uma exception for lançada ao criar o kafkaService, kafkaService.close é chamada
            //Se uma execption não for lanáda ao criar o kafkaSerive, kafkaService.close é chamada após kafkaService.run ser chamada
            try(var kafkaService = new KafkaService(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL", emailService::parse, String.class, Map.of())){
                kafkaService.run();
            }

        }

        private void parse(ConsumerRecord<String, String> record) {

            System.out.println("--------------------------------------------");
            System.out.println("Enviando email");
            System.out.println(record.key());
            System.out.println(record.value());
            System.out.println(record.partition());
            System.out.println(record.offset());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Email enviado");

        }


    }
