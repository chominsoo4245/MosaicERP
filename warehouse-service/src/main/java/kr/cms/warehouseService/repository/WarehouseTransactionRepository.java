package kr.cms.warehouseService.repository;

import kr.cms.warehouseService.entity.WarehouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseTransactionRepository extends JpaRepository<WarehouseTransaction, Long> {
    Optional<WarehouseTransaction> findByTransactionId(String transactionId);
}
