package kr.cms.inventoryService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;

import java.util.List;

public interface InventoryService {
    ApiResponse<InventoryDTO> getInventory(Long itemId, Integer warehouseId, Integer binId, Integer lotId, String ip, String userAgent, String loginId);
    ApiResponse<List<InventoryDTO>> getInventoryList(String ip, String userAgent, String loginId);
    ApiResponse<InventoryDTO> getInventoryDetail(Long inventoryId, String ip, String userAgent, String loginId);
    ApiResponse<InventoryDTO> increaseInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO, String ip, String userAgent, String loginId);
    ApiResponse<InventoryDTO> decreaseInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO, String ip, String userAgent, String loginId);
    ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(Long itemId, Integer warehouseId, Integer binId, Integer lotId, String ip, String userAgent, String loginId);

}
