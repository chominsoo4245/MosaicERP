package kr.cms.warehouseService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    private Long warehouseId;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
