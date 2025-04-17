package kr.cms.lotService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.service.LotTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lot-service")
@RequiredArgsConstructor
public class LotTransactionController {
    private final LotTransactionService transactionService;
    private final HeaderProvider headerProvider;

    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateLot(
            @PathVariable String transactionId,
            @RequestBody LotDTO lotDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryCreateLot(
                    transactionId,
                    lotDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare lot creation: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateLot(@PathVariable String transactionId, @RequestBody LotDTO lotDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmCreateLot(
                    transactionId,
                    lotDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm lot creation: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateLot(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelCreateLot(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel lot creation: " + e.getMessage());
        }
    }

    @PostMapping("/try/update/{transactionId}")
    public ApiResponse<String> tryUpdateLot(
            @PathVariable String transactionId,
            @RequestBody LotDTO lotDTO
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryUpdateLot(
                    transactionId,
                    lotDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare lot update: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateLot(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmUpdateLot(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm lot update: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateLot(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelUpdateLot(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel lot update: " + e.getMessage());
        }
    }

    @PostMapping("/try/delete/{transactionId}/{lotId}")
    public ApiResponse<String> tryDeleteLot(
            @PathVariable String transactionId,
            @PathVariable Long lotId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryDeleteLot(
                    transactionId,
                    lotId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare lot deletion: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/delete/{transactionId}")
    public ApiResponse<String> confirmDeleteLot(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmDeleteLot(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm lot deletion: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/delete/{transactionId}")
    public ApiResponse<String> cancelDeleteLot(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelDeleteLot(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel lot deletion: " + e.getMessage());
        }
    }
}
