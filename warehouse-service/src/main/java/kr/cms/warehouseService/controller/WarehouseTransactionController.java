package kr.cms.warehouseService.controller;

import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.service.WarehouseTransactionService;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse-service")
@RequiredArgsConstructor
public class WarehouseTransactionController {
    private final WarehouseTransactionService transactionService;
    private final HeaderProvider headerProvider;

    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateWarehouse(@PathVariable String transactionId, @RequestBody WarehouseDTO warehouseDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryCreateWarehouse(
                    transactionId,
                    warehouseDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare warehouse creation: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmCreateWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm warehouse creation: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelCreateWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel warehouse creation: " + e.getMessage());
        }
    }

    @PostMapping("/try/update/{transactionId}")
    public ApiResponse<String> tryUpdateWarehouse(@PathVariable String transactionId, @RequestBody WarehouseDTO warehouseDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryUpdateWarehouse(
                    transactionId,
                    warehouseDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare warehouse update: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmUpdateWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm warehouse update: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelUpdateWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel warehouse update: " + e.getMessage());
        }
    }

    @PostMapping("/try/delete/{transactionId}/{warehouseId}")
    public ApiResponse<String> tryDeleteWarehouse(@PathVariable String transactionId, @PathVariable Long warehouseId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryDeleteWarehouse(
                    transactionId,
                    warehouseId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare warehouse deletion: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/delete/{transactionId}")
    public ApiResponse<String> confirmDeleteWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmDeleteWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm warehouse deletion: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/delete/{transactionId}")
    public ApiResponse<String> cancelDeleteWarehouse(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelDeleteWarehouse(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel warehouse deletion: " + e.getMessage());
        }
    }
}
