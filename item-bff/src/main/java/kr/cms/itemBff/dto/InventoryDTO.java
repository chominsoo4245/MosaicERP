package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private Long inventoryId;
    private Long itemId;
    private Integer warehouseId;
    private Integer binId;
    private Integer lotId;
    private Integer currentQuantity;
    private Integer reservedQuantity;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}