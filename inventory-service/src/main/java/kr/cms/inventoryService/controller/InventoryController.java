package kr.cms.inventoryService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;
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

    @GetMapping
    public ApiResponse<InventoryDTO> getInventory(
            @RequestParam("itemId") Long itemId,
            @RequestParam("warehouseId") Integer warehouseId,
            @RequestParam(name = "binId", required = false) Integer binId,
            @RequestParam(name = "lotId", required = false) Integer lotId
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.getInventory(itemId, warehouseId, binId, lotId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/list")
    public ApiResponse<List<InventoryDTO>> getInventoryList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.getInventoryList(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/detail/{inventoryId}")
    public ApiResponse<InventoryDTO> getInventoryDetail(
            @RequestParam("inventoryId") Long inventoryId
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.getInventoryDetail(inventoryId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PostMapping("/increase")
    public ApiResponse<InventoryDTO> increaseInventory(
            @RequestBody InventoryUpdateRequestDTO inventoryUpdateRequestDTO
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.increaseInventory(inventoryUpdateRequestDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PostMapping("/decrease")
    public ApiResponse<InventoryDTO> decreaseInventory(
            @RequestBody InventoryUpdateRequestDTO inventoryUpdateRequestDTO
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.decreaseInventory(inventoryUpdateRequestDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/history")
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(
            @RequestParam("itemId") Long itemId,
            @RequestParam("warehouseId") Integer warehouseId,
            @RequestParam(name = "binId", required = false) Integer binId,
            @RequestParam(name = "lotId", required = false) Integer lotId
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryService.getInventoryHistory(itemId, warehouseId, binId, lotId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }
}
