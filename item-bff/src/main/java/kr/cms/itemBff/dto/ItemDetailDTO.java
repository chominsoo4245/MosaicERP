package kr.cms.itemBff.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailDTO {
    // 기본 품목 정보
    private Long itemId;
    private String code;
    private String name;
    private String description;
    private String unit;
    private BigDecimal cost;
    private BigDecimal price;
    private Boolean isLotTracked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 카테고리 정보
    private Integer categoryId;
    private String categoryName;

    // 공급업체 정보
    private Integer supplierId;
    private String supplierName;

    // LOT 정보 (LOT 관리 대상인 경우)
    private List<LotDTO> lots;

    // 재고 정보
    private List<InventoryDTO> inventories;

    // 추가 상세 정보 (필요시 확장)
    private String barcode;
    private String imageUrl;
    private String notes;
}