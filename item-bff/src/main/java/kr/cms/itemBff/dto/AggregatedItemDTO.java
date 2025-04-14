package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedItemDTO {
    private Long itemId;
    private String code;
    private String name;
    private String categoryName;
    private String description;
    private String unit;
    private BigDecimal cost;
    private BigDecimal price;
    private boolean isLotTracked;
    private String supplierName;

}
