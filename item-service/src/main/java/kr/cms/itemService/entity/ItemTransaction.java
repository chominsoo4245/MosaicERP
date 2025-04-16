package kr.cms.itemService.entity;

import jakarta.persistence.*;
import kr.cms.common.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tr_item")
@Getter
@Setter
public class ItemTransaction {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Lob
    @Column(name = "item_data")
    private String itemData;

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
