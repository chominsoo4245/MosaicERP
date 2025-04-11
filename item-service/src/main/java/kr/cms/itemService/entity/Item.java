package kr.cms.itemService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId; // 품목 분류 (tb_category의 식별자)

    @Column(nullable = false, length = 50)
    private String code;        // 품목 코드

    @Column(nullable = false, length = 200)
    private String name;        // 품목 명

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String unit;        // 단위 (예: EA, KG 등)

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;        // 원가

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 판매 가격

    @Column(name = "is_lot_tracked", nullable = false)
    private Boolean isLotTracked = false; // LOT 관리 여부

    @Column(name = "default_supplier_id")
    private Integer defaultSupplierId;    // 기본 공급업체 ID (tb_supplier 참조, 옵션)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}