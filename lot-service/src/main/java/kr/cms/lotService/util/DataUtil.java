package kr.cms.lotService.util;

import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.entity.Lot;

public class DataUtil {
    public static LotDTO convertToDTO(Lot entity) {
        LotDTO dto = new LotDTO();
        dto.setLotId(entity.getLotId());
        dto.setItemId(entity.getItemId());
        dto.setLotNumber(entity.getLotNumber());
        dto.setInitialStock(entity.getInitialStock());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setLocationInfo(entity.getLocationInfo());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Lot convertToEntity(LotDTO dto){
        Lot entity = new Lot();
        entity.setLotId(dto.getLotId());
        entity.setItemId(dto.getItemId());
        entity.setLotNumber(dto.getLotNumber());
        entity.setInitialStock(dto.getInitialStock());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setLocationInfo(dto.getLocationInfo());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
