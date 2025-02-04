package pl.ddconstriction.payment_service.endpoint.soap;

import com.example.soap.api.requests.CreatePaymentRequest;
import com.example.soap.api.requests.SendPaymentCorrectionRequest;
import com.example.soap.api.responses.CreatePaymentResponse;
import com.example.soap.api.responses.SendPaymentCorrectionResponse;
import com.example.soap.api.responses.Status;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PaymentEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/soap/api";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreatePaymentRequest")
    @ResponsePayload
    public CreatePaymentResponse createPayment(@RequestPayload CreatePaymentRequest request) {
        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setTransactionId(1L); // Example transaction ID
        response.setStatus(Status.PENDING);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendPaymentCorrectionRequest")
    @ResponsePayload
    public SendPaymentCorrectionResponse sendPaymentCorrection(@RequestPayload SendPaymentCorrectionRequest request) {
        // Validate transaction ID (e.g., check if it exists in the database)
        SendPaymentCorrectionResponse response = new SendPaymentCorrectionResponse();
        response.setStatus(Status.COMPLETED);
        return response;
    }
}