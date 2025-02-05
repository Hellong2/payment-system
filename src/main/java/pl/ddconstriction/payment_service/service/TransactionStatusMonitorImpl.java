package pl.ddconstriction.payment_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;
import pl.ddconstriction.payment_service.repository.PaymentTransactionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionStatusMonitorImpl implements TransactionStatusMonitor {

    private final PaymentTransactionRepository transactionRepository;
    private final TransactionEventPublisher eventPublisher;

    public TransactionStatusMonitorImpl(PaymentTransactionRepository transactionRepository, TransactionEventPublisherImpl eventPublisher) {
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    static final Logger log = LoggerFactory.getLogger(TransactionStatusMonitorImpl.class);

    @Scheduled(fixedRate = 30000)
    @Override
    public void monitorTransactions() {
        Instant thirtySecondsAgo = Instant.now().minusSeconds(30);



        try {
            log.info("Starting Transaction Monitoring Cycle at {}", LocalDateTime.now());

            List<PaymentTransaction> completedTransactions = transactionRepository.findByStatusAndUpdatedAtAfter(PaymentStatusEnum.COMPLETED, thirtySecondsAgo);
            log.info("Found {} completed transactions to publish.", completedTransactions.size());
            for (PaymentTransaction tx : completedTransactions) {
                String message = String.format("{\"id\": %d, \"amount\": %.2f, \"currency\": \"%s\", \"status\": \"%s\"}",
                        tx.getId(), tx.getAmount(), tx.getCurrency(), tx.getStatus());
                eventPublisher.publishTransactionCompleted(message);
            }

            List<PaymentTransaction> pendingTransactions = transactionRepository.findByStatusAndCreatedAtBefore(PaymentStatusEnum.PENDING, thirtySecondsAgo);
            log.info("Found {} pending transactions to expire.", pendingTransactions.size());

            for (PaymentTransaction tx : pendingTransactions) {
                tx.setStatus(PaymentStatusEnum.EXPIRED);
                transactionRepository.save(tx);

                String message = String.format("{\"id\": %d, \"amount\": %.2f, \"currency\": \"%s\", \"status\": \"%s\"}",
                        tx.getId(), tx.getAmount(), tx.getCurrency(), tx.getStatus());
                eventPublisher.publishTransactionExpired(message);
            }

            log.info("Transaction Monitoring Cycle completed successfully.");

        } catch (Exception e) {
            log.error("Error occurred during Transaction Monitoring Cycle: {}", e.getMessage(), e);
        }
    }
}