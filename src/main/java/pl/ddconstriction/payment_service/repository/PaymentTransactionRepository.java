package pl.ddconstriction.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ddconstriction.payment_service.entity.PaymentTransaction;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
}
