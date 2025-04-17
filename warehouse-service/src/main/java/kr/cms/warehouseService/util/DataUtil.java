package kr.cms.warehouseService.util;

import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.entity.Warehouse;

public class DataUtil {
    public static WarehouseDTO convertToDTO(Warehouse entity) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setName(entity.getName());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());
        dto.setCapacity(entity.getCapacity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Warehouse convertToEntity(WarehouseDTO dto){
        Warehouse entity = new Warehouse();
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setName(dto.getName());
        entity.setAddressLine1(dto.getAddressLine1());
        entity.setAddressLine2(dto.getAddressLine2());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setCapacity(dto.getCapacity());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
