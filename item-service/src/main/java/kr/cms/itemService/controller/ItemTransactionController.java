package kr.cms.itemService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.service.ItemTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item-service")
@RequiredArgsConstructor
public class ItemTransactionController {

    private final ItemTransactionService transactionService;
    private final HeaderProvider headerProvider;

    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateItem(
            @PathVariable String transactionId,
            @RequestBody ItemDTO itemDTO
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryCreateItem(
                    transactionId,
                    itemDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare item creation: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmCreateItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm item creation: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelCreateItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel item creation: " + e.getMessage());
        }
    }

    @PostMapping("/try/update/{transactionId}")
    public ApiResponse<String> tryUpdateItem(
            @PathVariable String transactionId,
            @RequestBody ItemDTO itemDTO
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryUpdateItem(
                    transactionId,
                    itemDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare item update: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmUpdateItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm item update: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelUpdateItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel item update: " + e.getMessage());
        }
    }

    @PostMapping("/try/delete/{transactionId}/{itemId}")
    public ApiResponse<String> tryDeleteItem(
            @PathVariable String transactionId,
            @PathVariable Long itemId
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryDeleteItem(
                    transactionId,
                    itemId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare item deletion: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/delete/{transactionId}")
    public ApiResponse<String> confirmDeleteItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmDeleteItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm item deletion: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/delete/{transactionId}")
    public ApiResponse<String> cancelDeleteItem(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelDeleteItem(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel item deletion: " + e.getMessage());
        }
    }
}
