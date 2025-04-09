package kr.cms.inventoryService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_inventory_history")
public class InventoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Integer warehouseId;

    private Integer binId;
    private String lotNumber;
    private LocalDateTime expirationDate;

    @Column(nullable = false, length = 20)
    private String transactionType;

    @Column(nullable = false)
    private Integer quantityChange;

    private Integer preQuantity;

    private Integer postQuantity;

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    private String originRef;

    private String operator;
}
