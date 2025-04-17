package kr.cms.binService.util;

import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.entity.Bin;

public class DataUtil {
    public static BinDTO convertToDTO(Bin entity) {
        BinDTO dto = new BinDTO();
        dto.setBinId(entity.getBinId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Bin convertToEntity(BinDTO dto){
        Bin entity = new Bin();
        entity.setBinId(dto.getBinId());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
