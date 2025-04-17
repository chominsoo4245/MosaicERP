package kr.cms.serialService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.service.SerialTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serial-service")
@RequiredArgsConstructor
public class SerialTransactionController {
    private final SerialTransactionService transactionService;
    private final HeaderProvider headerProvider;

    @PostMapping("/try/create/{transactionId}")
    public ApiResponse<String> tryCreateSerial(
            @PathVariable String transactionId,
            @RequestBody SerialDTO serialDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryCreateSerial(
                    transactionId,
                    serialDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare serial creation: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/create/{transactionId}")
    public ApiResponse<String> confirmCreateSerial(@PathVariable String transactionId, @RequestBody SerialDTO serialDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmCreateSerial(
                    transactionId,
                    serialDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm serial creation: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/create/{transactionId}")
    public ApiResponse<String> cancelCreateSerial(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelCreateSerial(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel serial creation: " + e.getMessage());
        }
    }

    @PostMapping("/try/update/{transactionId}")
    public ApiResponse<String> tryUpdateSerial(@PathVariable String transactionId, @RequestBody SerialDTO serialDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryUpdateSerial(
                    transactionId,
                    serialDTO,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare serial update: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/update/{transactionId}")
    public ApiResponse<String> confirmUpdateSerial(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmUpdateSerial(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm serial update: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/update/{transactionId}")
    public ApiResponse<String> cancelUpdateSerial(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelUpdateSerial(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel serial update: " + e.getMessage());
        }
    }

    @PostMapping("/try/delete/{transactionId}/{serialId}")
    public ApiResponse<String> tryDeleteSerial(@PathVariable String transactionId, @PathVariable Long serialId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.tryDeleteSerial(
                    transactionId,
                    serialId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to prepare serial deletion: " + e.getMessage());
        }
    }

    @PostMapping("/confirm/delete/{transactionId}")
    public ApiResponse<String> confirmDeleteSerial(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.confirmDeleteSerial(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to confirm serial deletion: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/delete/{transactionId}")
    public ApiResponse<String> cancelDeleteSerial(@PathVariable String transactionId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return transactionService.cancelDeleteSerial(
                    transactionId,
                    headerInfoDTO.getIp(),
                    headerInfoDTO.getUserAgent(),
                    headerInfoDTO.getLoginId()
            );
        } catch (Exception e) {
            return ApiResponse.fail("Failed to cancel serial deletion: " + e.getMessage());
        }
    }
}
