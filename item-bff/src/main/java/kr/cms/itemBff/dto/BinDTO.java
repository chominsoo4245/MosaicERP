package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinDTO {
    private Integer binId;
    private Integer warehouseId;
    private String binCode;
    private String description;
}