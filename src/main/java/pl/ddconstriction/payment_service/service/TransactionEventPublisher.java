package pl.ddconstriction.payment_service.service;

public interface TransactionEventPublisher {
    void publishMessage(String topic, String message);

    void publishTransactionExpired(String message);

    void publishTransactionCompleted(String message);
}