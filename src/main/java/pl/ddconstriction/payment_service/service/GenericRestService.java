package pl.ddconstriction.payment_service.service;

import java.util.List;

public interface GenericRestService<REQUEST, RESPONSE, ENTITY> {
    RESPONSE create(REQUEST paymentTransactionRequest);

    List<RESPONSE> findAll();

    RESPONSE findById(Long id);

    void deleteById(Long id);

    RESPONSE update(Long id, REQUEST paymentTransactionRequest);

    RESPONSE mapEntityToResponse(ENTITY paymentTransaction);
}
