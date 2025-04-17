package kr.cms.inventoryService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory-service")
@RequiredArgsConstructor
public class InventoryTransactionController {
    private final InventoryTransactionService inventoryTransactionService;
    private final HeaderProvider headerProvider;

    /**
     * Inventory 생성 준비(Try) API
     */
    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateInventory(
            @PathVariable String transactionId,
            @RequestBody InventoryDTO inventoryDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryTransactionService.tryCreateInventory(transactionId, inventoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 생성 확정(Confirm) API
     */
    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateInventory(
            @PathVariable String transactionId,
            @RequestBody InventoryDTO inventoryDTO) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return inventoryTransactionService.confirmCreateInventory(transactionId, inventoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 생성 취소(Cancel) API
     */
    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateInventory(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.cancelCreateInventory(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 업데이트 준비(Try) API
     */
    @PostMapping("/try/update/{transactionId}")
    public ApiResponse<String> tryUpdateInventory(
            @PathVariable String transactionId,
            @RequestBody InventoryDTO inventoryDTO) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.tryUpdateInventory(transactionId, inventoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 업데이트 확정(Confirm) API
     */
    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateInventory(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.confirmUpdateInventory(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 업데이트 취소(Cancel) API
     */
    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateInventory(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.cancelUpdateInventory(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 수량 증가 준비(Try) API
     */
    @PostMapping("/try/increase/{transactionId}/{inventoryId}")
    public ApiResponse<String> tryIncreaseInventory(
            @PathVariable String transactionId,
            @PathVariable Long inventoryId,
            @RequestParam Integer quantity) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.tryIncreaseInventory(transactionId, inventoryId, quantity, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 수량 증가 확정(Confirm) API
     */
    @PostMapping("/confirm/increase/{transactionId}")
    public ApiResponse<String> confirmIncreaseInventory(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.confirmIncreaseInventory(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 수량 감소 준비(Try) API
     */
    @PostMapping("/try/decrease/{transactionId}/{inventoryId}")
    public ApiResponse<String> tryDecreaseInventory(
            @PathVariable String transactionId,
            @PathVariable Long inventoryId,
            @RequestParam Integer quantity) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.tryDecreaseInventory(transactionId, inventoryId, quantity, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * Inventory 수량 감소 확정(Confirm) API
     */
    @PostMapping("/confirm/decrease/{transactionId}")
    public ApiResponse<String> confirmDecreaseInventory(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.confirmDecreaseInventory(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    /**
     * 모든 Inventory 작업 취소(Cancel) API
     */
    @PostMapping("/cancel/operation/{transactionId}")
    public ApiResponse<String> cancelInventoryOperation(
            @PathVariable String transactionId) {
                HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);

        return inventoryTransactionService.cancelInventoryOperation(transactionId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }
}
