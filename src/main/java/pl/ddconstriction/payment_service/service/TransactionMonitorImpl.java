package pl.ddconstriction.payment_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionMonitorImpl implements TransactionMonitor {
    private final TransactionMonitoringService monitoringService;

    static final Logger log = LoggerFactory.getLogger(TransactionMonitorImpl.class);

    @Override
    @Scheduled(fixedRate = 30000)
    public void monitorTransactions() {
        Instant threshold = Instant.now().minusSeconds(30);
        log.info("Starting Transaction Monitoring Cycle at {}", LocalDateTime.now());
        try {
            monitoringService.processCompletedTransactions(threshold);
            monitoringService.processPendingTransactions(threshold);
        } catch (Exception e) {
            log.error("Error occurred during Transaction Monitoring Cycle: {}", e.getMessage(), e);
        }
        log.info("Transaction Monitoring Cycle completed successfully.");
    }
}