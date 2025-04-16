package kr.cms.serialService.util;

import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.entity.Serial;

public class DataUtil {
    public static SerialDTO convertToDTO(Serial entity) {
        SerialDTO dto = new SerialDTO();
        dto.setSerialId(entity.getSerialId());
        dto.setItemId(entity.getItemId());
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Serial convertToEntity(SerialDTO dto){
        Serial entity = new Serial();
        entity.setSerialId(dto.getSerialId());
        entity.setItemId(dto.getItemId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
