package kr.cms.lotService.entity;

import jakarta.persistence.*;
import kr.cms.common.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tr_lot")
@Getter
@Setter
public class LotTransaction {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Lob
    @Column(name = "lot_data")
    private String lotData;

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
