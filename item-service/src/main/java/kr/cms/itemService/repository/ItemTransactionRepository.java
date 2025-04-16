package kr.cms.itemService.repository;

import kr.cms.itemService.entity.ItemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {
    Optional<ItemTransaction> findByTransactionId(String transactionId);
}
