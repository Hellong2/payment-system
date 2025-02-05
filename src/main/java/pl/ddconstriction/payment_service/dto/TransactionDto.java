package pl.ddconstriction.payment_service.dto;

import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;

import java.math.BigDecimal;

public record TransactionDto (Long id,
                              BigDecimal amount,
                              String currency,
                              PaymentStatusEnum status){
}
