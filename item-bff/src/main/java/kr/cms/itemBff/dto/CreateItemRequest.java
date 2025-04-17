package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {
    private Integer categoryId;
    private String code;
    private String name;
    private String description;
    private String unit;
    private BigDecimal cost;
    private BigDecimal price;
    private Boolean isLotTracked;
    private Integer defaultSupplierId;

    // Lot 관련 필드
    private LocalDate expiryDate;

    // Inventory 관련 필드
    private Integer warehouseId;
    private Integer binId;
    private Integer initialQuantity;

    /**
     * ItemDTO로 변환
     */
    public ItemDTO toItemDTO() {
        return ItemDTO.builder()
                .categoryId(categoryId)
                .code(code)
                .name(name)
                .description(description)
                .unit(unit)
                .cost(cost)
                .price(price)
                .isLotTracked(isLotTracked)
                .defaultSupplierId(defaultSupplierId)
                .build();
    }

    public LotDTO toLotDTO() {
        return LotDTO.builder()
                .expiryDate(expiryDate)
                .build();
    }

    public SerialDTO toSerialDTO() {
        return SerialDTO.builder().build();
    }

    public InventoryDTO toInventoryDTO() {
        return InventoryDTO.builder()
                .warehouseId(warehouseId)
                .binId(binId)
                .currentQuantity(initialQuantity != null ? initialQuantity : 0)
                .reservedQuantity(0)
                .build();
    }
}