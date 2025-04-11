package kr.cms.inventoryBff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedInventoryDTO {
    private Long inventoryId;
    // ITEM DATA
    private Long itemId;
    private String itemName;
    // WAREHOUSE DATA
    private Integer warehouseId;
    private String warehouseName;
    // BIN DATA
    private Integer binId;
    private String binName;
    // LOT DATA
    private Integer lotId;
    private String lotNumber;
    private LocalDateTime expirationDate;
}
