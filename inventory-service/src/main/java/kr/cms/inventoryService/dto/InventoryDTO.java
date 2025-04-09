package kr.cms.inventoryService.dto;

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
    private String lotNumber;
    private LocalDateTime expirationDate;
    private Integer currentQuantity;
    private Integer reservedQuantity;
    private LocalDateTime lastUpdated;
    private int version;
}
