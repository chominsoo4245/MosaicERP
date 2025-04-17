package kr.cms.inventoryService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;
import kr.cms.inventoryService.dto.SearchDataDTO;
import kr.cms.inventoryService.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory-service")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<InventoryDTO> getInventory(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return inventoryService.getInventory(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieve inventory: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<InventoryDTO>> getInventoryList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return inventoryService.getAllInventory(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieved categories: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<InventoryDTO>> getSearchInventoryList(@RequestBody SearchDataDTO dto) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return inventoryService.getSearchInventories(dto, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to search inventories: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(
            @RequestParam("itemId") Long itemId,
            @RequestParam("warehouseId") Integer warehouseId,
            @RequestParam(name = "binId", required = false) Integer binId,
            @RequestParam(name = "lotId", required = false) Integer lotId
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return inventoryService.getInventoryHistory(itemId, warehouseId, binId, lotId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
    }
}
