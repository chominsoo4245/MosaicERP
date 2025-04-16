package kr.cms.lotService.repository;

import kr.cms.lotService.entity.LotTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotTransactionRepository extends JpaRepository<LotTransaction, Long> {
    Optional<LotTransaction> findByTransactionId(String transactionId);
}
