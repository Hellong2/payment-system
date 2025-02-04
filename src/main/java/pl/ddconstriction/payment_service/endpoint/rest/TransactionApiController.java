package pl.ddconstriction.payment_service.endpoint.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import pl.ddconstriction.payment_service.api.controller.TransactionsApi;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionRequest;
import pl.ddconstriction.payment_service.api.model.PaymentTransactionResponse;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionApiController implements TransactionsApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return TransactionsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> createTransaction(PaymentTransactionRequest request) {
        PaymentTransactionResponse response = new PaymentTransactionResponse();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(Integer id) {
        return TransactionsApi.super.deleteTransaction(id);
    }

    @Override
    public ResponseEntity<List<PaymentTransactionResponse>> getAllTransactions() {
        return TransactionsApi.super.getAllTransactions();
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> getTransactionById(Integer id) {
        PaymentTransactionResponse response = new PaymentTransactionResponse();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PaymentTransactionResponse> updateTransaction(Integer id, PaymentTransactionRequest paymentTransactionRequest) {
        return TransactionsApi.super.updateTransaction(id, paymentTransactionRequest);
    }

}
