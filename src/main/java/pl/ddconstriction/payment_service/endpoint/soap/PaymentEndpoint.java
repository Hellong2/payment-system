package pl.ddconstriction.payment_service.endpoint.soap;

import com.example.soap.api.requests.CreatePaymentRequest;
import com.example.soap.api.requests.SendPaymentCorrectionRequest;
import com.example.soap.api.responses.CreatePaymentResponse;
import com.example.soap.api.responses.SendPaymentCorrectionResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.ddconstriction.payment_service.service.PaymentTransactionService;

@Endpoint
public class PaymentEndpoint {
    private final PaymentTransactionService transactionService;

    public PaymentEndpoint(PaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private static final String NAMESPACE_URI = "http://example.com/soap/api";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreatePaymentRequest")
    @ResponsePayload
    public CreatePaymentResponse createPayment(@RequestPayload CreatePaymentRequest request) {
        return transactionService.create(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendPaymentCorrectionRequest")
    @ResponsePayload
    public SendPaymentCorrectionResponse sendPaymentCorrection(@RequestPayload SendPaymentCorrectionRequest request) {
        return transactionService.correctPayment(request);
    }
}