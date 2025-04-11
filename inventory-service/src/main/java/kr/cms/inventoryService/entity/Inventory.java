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
@Table(name = "tb_inventory", uniqueConstraints = {@UniqueConstraint(columnNames = {"itemId", "warehouseId"})})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Integer warehouseId;

    private Integer binId;
    private Integer lotId;

    @Column(nullable = false)
    private Integer currentQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Version
    private int version;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
