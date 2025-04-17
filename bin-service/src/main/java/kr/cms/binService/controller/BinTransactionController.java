package kr.cms.binService.controller;

import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.service.BinTransactionService;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bin-service")
@RequiredArgsConstructor
public class BinTransactionController {
    private final BinTransactionService transactionService;
    private final HeaderProvider headerProvider;

    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateBin(@PathVariable String transactionId, @RequestBody BinDTO binDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryCreateBin(
                    transactionId,
                    binDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare bin creation: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmCreateBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm bin creation: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelCreateBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel bin creation: " + e.getMessage());
        }
    }

    @PostMapping("/try/update/{transactionId}}")
    public ApiResponse<String> tryUpdateBin(@PathVariable String transactionId, @RequestBody BinDTO binDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryUpdateBin(
                    transactionId,
                    binDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare bin update: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmUpdateBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm bin update: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelUpdateBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel bin update: " + e.getMessage());
        }
    }

    @PostMapping("/try/delete/{transactionId}/{binId}")
    public ApiResponse<String> tryDeleteBin(@PathVariable String transactionId, @PathVariable Long binId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryDeleteBin(
                    transactionId,
                    binId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare bin deletion: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/delete/{transactionId}")
    public ApiResponse<String> confirmDeleteBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmDeleteBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm bin deletion: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/delete/{transactionId}")
    public ApiResponse<String> cancelDeleteBin(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelDeleteBin(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel bin deletion: " + e.getMessage());
        }
    }
}
