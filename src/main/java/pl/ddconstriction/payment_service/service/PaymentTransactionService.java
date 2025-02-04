package pl.ddconstriction.payment_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;
import pl.ddconstriction.payment_service.repository.PaymentTransactionRepository;

import java.math.BigDecimal;

@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository repo;

    public PaymentTransactionService(PaymentTransactionRepository repo) {
        this.repo = repo;
    }

    public PaymentTransaction createPaymentTransaction(BigDecimal amount, String currency, PaymentStatusEnum status) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setStatus(status);
        return repo.save(transaction);
    }
}