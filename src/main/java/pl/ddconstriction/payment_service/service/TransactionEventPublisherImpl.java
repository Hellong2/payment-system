package pl.ddconstriction.payment_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class TransactionEventPublisherImpl implements TransactionEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    static final Logger log = LoggerFactory.getLogger(TransactionEventPublisherImpl.class);

    public TransactionEventPublisherImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishTransactionCompleted(String message) {
        publishMessage("completed-transactions", message);
    }

    @Override
    public void publishTransactionExpired(String message) {
        publishMessage("expired-transactions", message);
    }

    @Override
    public void publishMessage(String topic, String message) {
        try {
            log.info("Publishing message to topic [{}]: {}", topic, message);

            CompletableFuture<?> future = kafkaTemplate.send(topic, message);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message [{}] to topic [{}]: {}", message, topic, ex.getMessage(), ex);
                } else {
                    log.info("Message [{}] sent successfully to topic [{}]", message, topic);
                }
            });
        } catch (Exception e) {
            log.error("Unexpected error while publishing message to topic [{}]: {}", topic, e.getMessage(), e);
        }
    }

    public void testConnection() {
        try {
            kafkaTemplate.send("completed-transactions", "Connection Test").get();
            log.info("Kafka connection successful!");
        } catch (Exception e) {
            log.info("Kafka connection failed: {}", e.getMessage());
        }
    }
}