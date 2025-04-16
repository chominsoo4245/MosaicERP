package kr.cms.serialService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.dto.SearchDataDTO;
import kr.cms.serialService.service.SerialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serial-service")
@RequiredArgsConstructor
public class SerialController {
    private final SerialService serialService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<SerialDTO> getSerial(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return serialService.getSerial(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve serial: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<SerialDTO>> getSerialList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return serialService.getAllSerial(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve serials: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<SerialDTO>> getSearchSerial(@RequestBody SearchDataDTO searchDataDTO){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return serialService.getSearchSerials(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search serials: " + e.getMessage());
        }
    }
}
