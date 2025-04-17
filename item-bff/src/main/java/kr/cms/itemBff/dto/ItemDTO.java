package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long itemId;
    private Integer categoryId;
    private String code;
    private String name;
    private String description;
    private String unit;
    private BigDecimal cost;
    private BigDecimal price;
    private Boolean isLotTracked;
    private Integer defaultSupplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
