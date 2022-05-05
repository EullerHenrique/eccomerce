import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Se uma exception for lançada ao criar o KafkaDispatcher, KafkaDispatcher.close é chamada
        //Se uma execption não for lanáda ao criar o KafkaDispatcher, KafkaDispatcher.close é chamada após kafkaService.run ser chamada
        try (var dispatcher = new KafkaDispatcher()){

            for (var i = 0; i < 10; i++) {

                var key = UUID.randomUUID().toString();
                var value = key + "PS5";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

                var email = "eullerhenrique@ufu.br";
                dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);

            }

        }

    }


}
