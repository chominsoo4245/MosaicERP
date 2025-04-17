package kr.cms.binService.controller;

import kr.cms.binService.service.BinService;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.dto.SearchDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bin-service")
@RequiredArgsConstructor
public class BinController {
    private final BinService binService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<BinDTO> getBin(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return binService.getBin(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve bin: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<BinDTO>> getBinList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return binService.getAllBin(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve bins: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<BinDTO>> getSearchBin(@RequestBody SearchDataDTO searchDataDTO){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return binService.getSearchBins(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search bins: " + e.getMessage());
        }
    }
}
