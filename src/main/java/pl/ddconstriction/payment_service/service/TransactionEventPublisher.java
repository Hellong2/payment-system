package pl.ddconstriction.payment_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class TransactionEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    static final Logger log = LoggerFactory.getLogger(TransactionEventPublisher.class);

    public TransactionEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTransactionCompleted(String message) {
        publishMessage("transactions-completed", message);
    }

    public void publishTransactionExpired(String message) {
        publishMessage("transactions-expired", message);
    }

    private void publishMessage(String topic, String message) {
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
}