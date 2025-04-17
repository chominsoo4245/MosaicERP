package kr.cms.inventoryService.entity;

import jakarta.persistence.*;
import kr.cms.common.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tr_inventory")
@Getter
@Setter
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "inventory_data", columnDefinition = "TEXT")
    private String inventoryData;

    @Column(name = "original_data", columnDefinition = "TEXT")
    private String originalData;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private String operation;

    @Column(name = "result_id")
    private String resultId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;
}