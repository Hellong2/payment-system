package pl.ddconstriction.payment_service.service;

import com.example.soap.api.requests.CreatePaymentRequest;
import com.example.soap.api.requests.SendPaymentCorrectionRequest;
import com.example.soap.api.responses.CreatePaymentResponse;
import com.example.soap.api.responses.ErrorMessage;
import com.example.soap.api.responses.SendPaymentCorrectionResponse;
import com.example.soap.api.responses.Status;
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
public class PaymentTransactionService {

    private final PaymentTransactionRepository repo;

    public PaymentTransactionService(PaymentTransactionRepository repo) {
        this.repo = repo;
    }

    public PaymentTransactionResponse create(PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(BigDecimal.valueOf(paymentTransactionRequest.getAmount()));
        transaction.setStatus(PaymentStatusEnum.PENDING);
        PaymentTransaction entity = repo.save(transaction);
        return mapEntityToResponse(entity);
    }

    public List<PaymentTransactionResponse> findAll() {
        return repo.findAll().stream().map(this::mapEntityToResponse).toList();
    }

    public PaymentTransactionResponse findById(Long id) {
        return mapEntityToResponse(repo.findById(id).orElse(null));
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public PaymentTransactionResponse update(Long id, PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction transaction = repo.findById(id).orElse(null);
        if (transaction == null) {
            throw  new TransactionNotFoundException("Transaction with id:" + id + " not found");
        }
        transaction.setAmount(BigDecimal.valueOf(paymentTransactionRequest.getAmount()));
        transaction.setStatus(PaymentStatusEnum.fromValue(paymentTransactionRequest.getStatus().getValue()));
        PaymentTransaction updatedEntity = repo.save(transaction);
        return mapEntityToResponse(updatedEntity);
    }

    public PaymentTransactionResponse mapEntityToResponse(PaymentTransaction paymentTransaction) {
        if (paymentTransaction == null) {
            return null;
        }
        return ObjectMapperUtils.map(paymentTransaction, PaymentTransactionResponse.class);
    }

    public CreatePaymentResponse create(CreatePaymentRequest request) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(request.getAmount());
        transaction.setStatus(PaymentStatusEnum.PENDING);
        PaymentTransaction entity = repo.save(transaction);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setTransactionId(entity.getId());
        response.setStatus(Status.valueOf(entity.getStatus().getValue()));
        return response;
    }

    public SendPaymentCorrectionResponse correctPayment(SendPaymentCorrectionRequest request) {
        SendPaymentCorrectionResponse response = new SendPaymentCorrectionResponse();
        PaymentTransaction transaction = repo.findById(request.getTransactionId()).orElse(null);

        if (transaction == null) {
            ErrorMessage error = new ErrorMessage();
            error.setCode("104");
            error.setMessage("Transaction with id:" + request.getTransactionId() + " not found");
            response.setError(error);
            return response;
        }

        transaction.setAmount(request.getCorrectionAmount());
        PaymentTransaction updatedEntity = repo.save(transaction);
        response.setStatus(Status.fromValue(updatedEntity.getStatus().getValue()));
        return response;
    }
}