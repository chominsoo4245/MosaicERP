package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemResponse {
    private Long itemId;
    private Long lotId;
    private String lotNumber;
    private Long serialId;
    private String serialNumber;
    private Long inventoryId;
}