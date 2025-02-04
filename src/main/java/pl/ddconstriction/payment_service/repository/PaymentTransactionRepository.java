package pl.ddconstriction.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ddconstriction.payment_service.entity.PaymentStatusEnum;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;

import java.time.Instant;
import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByStatusAndUpdatedAtAfter(PaymentStatusEnum status, Instant updatedTime);

    List<PaymentTransaction> findByStatusAndCreatedAtBefore(PaymentStatusEnum status, Instant createdTime);
}
