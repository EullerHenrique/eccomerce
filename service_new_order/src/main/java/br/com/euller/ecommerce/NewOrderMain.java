package br.com.euller.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Se uma exception for lançada ao criar o br.com.euller.ecommerce.KafkaDispatcher, br.com.euller.ecommerce.KafkaDispatcher.close é chamada
        //Se uma execption não for lanáda ao criar o br.com.euller.ecommerce.KafkaDispatcher, br.com.euller.ecommerce.KafkaDispatcher.close é chamada após kafkaService.run ser chamada
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {
                for (var i = 0; i < 10; i++) {

                    var userID = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                    var order = new Order(userID, orderId, amount);

                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userID, order);

                    var email = "eullerhenrique@outlook.com";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userID, email);

                }

            }

        }
    }
}

