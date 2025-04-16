package kr.cms.supplierService.repository;

import kr.cms.supplierService.entity.SupplierTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierTransactionRepository extends JpaRepository<SupplierTransaction, Long> {
    Optional<SupplierTransaction> findByTransactionId(String transactionId);
}
