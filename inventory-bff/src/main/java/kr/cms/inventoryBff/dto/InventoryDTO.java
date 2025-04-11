package kr.cms.inventoryBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
