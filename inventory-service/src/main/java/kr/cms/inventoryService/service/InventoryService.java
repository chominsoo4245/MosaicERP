package kr.cms.inventoryService.service;

import jakarta.transaction.Transactional;
import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;

import java.util.List;

public interface InventoryService {
    ApiResponse<InventoryDTO> getInventory(Long itemId, Integer warehouseId, Integer binId, String lotNumber, String ip, String userAgent, String loginId);
    ApiResponse<InventoryDTO> increaseInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO, String ip, String userAgent, String loginId);
    ApiResponse<InventoryDTO> decreaseInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO, String ip, String userAgent, String loginId);
    ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(Long itemId, Integer warehouseId, Integer binId, String lotNumber, String ip, String userAgent, String loginId);
}
