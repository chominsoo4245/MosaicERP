package kr.cms.supplierService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private String name;
    private String contactDetails;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
