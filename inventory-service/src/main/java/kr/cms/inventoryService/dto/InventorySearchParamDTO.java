package kr.cms.inventoryService.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventorySearchParamDTO {
    private Long itemId;
    private Integer warehouseId;
    private LocalDateTime fromCreatedAt;
    private LocalDateTime toCreatedAt;
    private String keyword;
}
