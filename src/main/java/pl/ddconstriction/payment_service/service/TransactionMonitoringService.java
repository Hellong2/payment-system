package pl.ddconstriction.payment_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ddconstriction.payment_service.dto.TransactionDto;
import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;
import pl.ddconstriction.payment_service.repository.PaymentTransactionRepository;
import pl.ddconstriction.payment_service.utils.JacksonMapper;
import pl.ddconstriction.payment_service.utils.ObjectMapperUtils;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionMonitoringService {
    private final PaymentTransactionRepository transactionRepository;
    private final TransactionEventPublisher eventPublisher;


    static final Logger log = LoggerFactory.getLogger(TransactionMonitoringService.class);


    @Transactional
    public void processCompletedTransactions(Instant threshold) {

        List<PaymentTransaction> transactions = transactionRepository
                .findByStatusAndUpdatedAtAfter(PaymentStatusEnum.COMPLETED, threshold);

        log.info("Found {} completed transactions to publish.", transactions.size());

        List<TransactionDto> transactionDtos = transactions.stream().map(this::mapEntityToDTO).toList();

        transactionDtos.forEach(this::publishCompletedTransaction);
    }

    @Transactional
    public void processPendingTransactions(Instant threshold) {
        List<PaymentTransaction> transactions = transactionRepository
                .findByStatusAndCreatedAtBefore(PaymentStatusEnum.PENDING, threshold);
        log.info("Found {} pending transactions to expire.", transactions.size());

        transactions.forEach(tx -> {
                    tx.setStatus(PaymentStatusEnum.EXPIRED);
                    transactionRepository.save(tx);
                });

        List<TransactionDto> transactionDtos = transactions.stream().map(this::mapEntityToDTO).toList();

        transactionDtos.forEach(this::publishExpiredTransaction);
    }

    private void publishCompletedTransaction(TransactionDto tx) {
        eventPublisher.publishTransactionCompleted(JacksonMapper.serializeToJson(tx));
    }

    private void publishExpiredTransaction(TransactionDto tx) {
        eventPublisher.publishTransactionExpired(JacksonMapper.serializeToJson(tx));
    }

    private TransactionDto mapEntityToDTO(PaymentTransaction entity) {
        if (entity == null) {
            return null;
        }
        return ObjectMapperUtils.map(entity, TransactionDto.class);
    }
}