package kr.cms.supplierService.util;

import kr.cms.supplierService.dto.SupplierDTO;
import kr.cms.supplierService.entity.Supplier;

public class DataUtil {
    public static SupplierDTO convertToDTO(Supplier entity) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setContactDetails(entity.getContactDetails());
        dto.setAddress(entity.getAddress());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Supplier convertToEntity(SupplierDTO dto) {
        Supplier entity = new Supplier();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setContactDetails(dto.getContactDetails());
        entity.setAddress(dto.getAddress());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
