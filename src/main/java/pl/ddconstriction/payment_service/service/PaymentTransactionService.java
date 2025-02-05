package pl.ddconstriction.payment_service.service;

import org.springframework.stereotype.Service;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionRequest;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionResponse;
import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;
import pl.ddconstriction.payment_service.exceptions.TransactionNotFoundException;
import pl.ddconstriction.payment_service.repository.PaymentTransactionRepository;
import pl.ddconstriction.payment_service.utils.ObjectMapperUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentTransactionService implements GenericRestService<PaymentTransactionRequest, PaymentTransactionResponse, PaymentTransaction> {

    private final PaymentTransactionRepository repo;

    public PaymentTransactionService(PaymentTransactionRepository repo) {
        this.repo = repo;
    }

    @Override
    public PaymentTransactionResponse create(PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(BigDecimal.valueOf(paymentTransactionRequest.getAmount()));
        transaction.setStatus(PaymentStatusEnum.PENDING);
        PaymentTransaction entity = repo.save(transaction);
        return mapEntityToResponse(entity);
    }

    @Override
    public List<PaymentTransactionResponse> findAll() {
        return repo.findAll().stream().map(this::mapEntityToResponse).toList();
    }

    @Override
    public PaymentTransactionResponse findById(Long id) {
        return mapEntityToResponse(repo.findById(id).orElse(null));
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public PaymentTransactionResponse update(Long id, PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction transaction = repo.findById(id).orElse(null);
        if (transaction == null) {
            throw  new TransactionNotFoundException("Transaction with id:" + id + " not found");
        }
        transaction.setAmount(BigDecimal.valueOf(paymentTransactionRequest.getAmount()));
        transaction.setStatus(PaymentStatusEnum.fromValue(paymentTransactionRequest.getStatus().getValue()));
        PaymentTransaction entity = repo.save(transaction);
        return mapEntityToResponse(entity);
    }

    @Override
    public PaymentTransactionResponse mapEntityToResponse(PaymentTransaction paymentTransaction) {
        if (paymentTransaction == null) {
            return null;
        }
        return ObjectMapperUtils.map(paymentTransaction, PaymentTransactionResponse.class);
    }

}