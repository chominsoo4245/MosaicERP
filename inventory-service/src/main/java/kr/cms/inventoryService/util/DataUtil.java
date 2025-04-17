package kr.cms.inventoryService.util;

import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.entity.Inventory;
import kr.cms.inventoryService.entity.InventoryHistory;

public class DataUtil {

    public static Inventory convertToEntity(InventoryDTO dto) {
        Inventory entity = new Inventory();
        entity.setInventoryId(dto.getInventoryId());
        entity.setItemId(dto.getItemId());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setBinId(dto.getBinId());
        entity.setLotId(dto.getLotId());
        entity.setCurrentQuantity(dto.getCurrentQuantity());
        entity.setReservedQuantity(dto.getReservedQuantity());
        entity.setVersion(dto.getVersion());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static InventoryDTO convertToDTO(Inventory entity) {
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(entity.getInventoryId());
        dto.setItemId(entity.getItemId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setBinId(entity.getBinId());
        dto.setLotId(entity.getLotId());
        dto.setCurrentQuantity(entity.getCurrentQuantity());
        dto.setReservedQuantity(entity.getReservedQuantity());
        dto.setVersion(entity.getVersion());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static InventoryHistoryDTO convertToInventoryHistoryDTO(InventoryHistory history) {
        return new InventoryHistoryDTO(
                history.getTransactionId(),
                history.getItemId(),
                history.getWarehouseId(),
                history.getBinId(),
                history.getLotId(),
                history.getTransactionType(),
                history.getQuantityChange(),
                history.getPreQuantity(),
                history.getPostQuantity(),
                history.getTransactionDate(),
                history.getOriginRef(),
                history.getOperator(),
                history.getCreatedAt(),
                history.getUpdatedAt()
        );
    }
}
