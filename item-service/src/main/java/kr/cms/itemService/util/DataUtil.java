package kr.cms.itemService.util;

import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.entity.Item;

public class DataUtil {
    public static Item convertToEntity(ItemDTO dto) {
        Item entity = new Item();
        entity.setItemId(entity.getItemId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setCost(dto.getCost());
        entity.setPrice(dto.getPrice());
        entity.setIsLotTracked(dto.getIsLotTracked() != null ? dto.getIsLotTracked() : false);
        entity.setDefaultSupplierId(dto.getDefaultSupplierId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static ItemDTO convertToDTO(Item entity) {
        ItemDTO dto = new ItemDTO();
        dto.setItemId(entity.getItemId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUnit(entity.getUnit());
        dto.setCost(entity.getCost());
        dto.setPrice(entity.getPrice());
        dto.setIsLotTracked(entity.getIsLotTracked() != null ? entity.getIsLotTracked() : false);
        dto.setDefaultSupplierId(entity.getDefaultSupplierId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
