package kr.cms.inventoryService.repository;

import kr.cms.inventoryService.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    Optional<InventoryTransaction> findByTransactionId(String transactionId);
}
