package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    private Integer warehouseId;
    private String name;
    private String location;
}