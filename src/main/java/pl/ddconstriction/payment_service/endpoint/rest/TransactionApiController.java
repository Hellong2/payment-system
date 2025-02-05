package pl.ddconstriction.payment_service.endpoint.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import pl.ddconstriction.payment_service.api.controller.TransactionsApi;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionRequest;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionResponse;
import pl.ddconstriction.payment_service.service.PaymentTransactionService;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionApiController implements TransactionsApi {

    private final PaymentTransactionService transactionService;

    public TransactionApiController(PaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return TransactionsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> createTransaction(PaymentTransactionRequest request) {
        PaymentTransactionResponse response = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(Integer id) {
        transactionService.deleteById(id.longValue());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PaymentTransactionResponse>> getAllTransactions() {
        List<PaymentTransactionResponse> responses = transactionService.findAll();
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> getTransactionById(Integer id) {
        PaymentTransactionResponse response = transactionService.findById(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> updateTransaction(Integer id, PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransactionResponse response = transactionService.update(id.longValue(), paymentTransactionRequest);
        return ResponseEntity.ok(response);
    }
}