package kr.cms.inventoryService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryDTO {
    private Long transactionId;
    private Long itemId;
    private Integer warehouseId;
    private Integer binId;
    private String lotNumber;
    private LocalDateTime expirationDate;
    private String transactionType;
    private Integer quantityChange;
    private Integer preQuantity;
    private Integer postQuantity;
    private LocalDateTime transactionDate;
    private String originRef;
    private String operator;
}
