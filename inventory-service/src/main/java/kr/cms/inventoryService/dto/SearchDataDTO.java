package kr.cms.inventoryService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchDataDTO {
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer minReserved;
    private Integer maxReserved;
    private LocalDateTime createStart;
    private LocalDateTime createEnd;
    private LocalDateTime updateStart;
    private LocalDateTime updateEnd;
    private Integer ItemId;
    private Integer warehouseId;
    private Integer binId;
    private Integer lotId;
}
