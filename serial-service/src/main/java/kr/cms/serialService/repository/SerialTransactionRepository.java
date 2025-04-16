package kr.cms.serialService.repository;

import kr.cms.serialService.entity.SerialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerialTransactionRepository extends JpaRepository<SerialTransaction, Long> {
    Optional<SerialTransaction> findByTransactionId(String transactionId);
}
