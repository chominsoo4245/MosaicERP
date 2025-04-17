package kr.cms.binService.entity;

import jakarta.persistence.*;
import kr.cms.common.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tr_bin")
@Getter
@Setter
public class BinTransaction {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Lob
    @Column(name = "bin_data")
    private String binData;

    @Lob
    @Column(name = "original_data")
    private String originalData;

    @Column(name = "operation")
    private String operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @Column(name = "result_id")
    private String resultId;
}
