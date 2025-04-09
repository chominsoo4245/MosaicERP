package kr.cms.inventoryService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryUpdateRequestDTO {
    private Long itemId;
    private Integer warehouseId;
    private Integer binId;
    private String LotNumber;
    private LocalDateTime expirationDate;
    private int quantity;
    private String originRef;
}
