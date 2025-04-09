package kr.cms.inventoryService.repository;

import kr.cms.inventoryService.dto.InventorySearchParamDTO;
import kr.cms.inventoryService.entity.Inventory;

import java.util.List;

public interface InventoryRepositoryCustom {
    List<Inventory> searchByCriteria(InventorySearchParamDTO param);
}
