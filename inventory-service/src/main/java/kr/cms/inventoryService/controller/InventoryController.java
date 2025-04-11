package kr.cms.inventoryService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;
import kr.cms.inventoryService.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventory-service")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ApiResponse<InventoryDTO> getInventory(
            @RequestParam("itemId") Long itemId,
            @RequestParam("warehouseId") Integer warehouseId,
            @RequestParam(name = "binId", required = false) Integer binId,
            @RequestParam(name = "lotId", required = false) Integer lotId,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.getInventory(itemId, warehouseId, binId, lotId, ip, userAgent, loginId);
    }

    @GetMapping("/list")
    public ApiResponse<List<InventoryDTO>> getInventoryList(
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.getInventoryList(ip, userAgent, loginId);
    }

    @GetMapping("/detail/{inventoryId}")
    public ApiResponse<InventoryDTO> getInventoryDetail(
            @RequestParam("inventoryId") Long inventoryId,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.getInventoryDetail(inventoryId, ip, userAgent, loginId);
    }

    @PostMapping("/increase")
    public ApiResponse<InventoryDTO> increaseInventory(
            @RequestBody InventoryUpdateRequestDTO inventoryUpdateRequestDTO,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.increaseInventory(inventoryUpdateRequestDTO, ip, userAgent, loginId);
    }

    @PostMapping("/decrease")
    public ApiResponse<InventoryDTO> decreaseInventory(
            @RequestBody InventoryUpdateRequestDTO inventoryUpdateRequestDTO,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.decreaseInventory(inventoryUpdateRequestDTO, ip, userAgent, loginId);
    }

    @GetMapping("/history")
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(
            @RequestParam("itemId") Long itemId,
            @RequestParam("warehouseId") Integer warehouseId,
            @RequestParam(name = "binId", required = false) Integer binId,
            @RequestParam(name = "lotId", required = false) Integer lotId,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];
        return inventoryService.getInventoryHistory(itemId, warehouseId, binId, lotId, ip, userAgent, loginId);
    }
}
