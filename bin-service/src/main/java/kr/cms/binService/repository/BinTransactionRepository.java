package kr.cms.binService.repository;

import kr.cms.binService.entity.BinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinTransactionRepository extends JpaRepository<BinTransaction, Long> {
    Optional<BinTransaction> findByTransactionId(String transactionId);
}
