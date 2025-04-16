package kr.cms.lotService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.dto.SearchDataDTO;
import kr.cms.lotService.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lot-service")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<LotDTO> getLot(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return lotService.getLot(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve lot: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<LotDTO>> getLotList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return lotService.getAllLot(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve lots: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<LotDTO>> getSearchLot(@RequestBody SearchDataDTO searchDataDTO){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return lotService.getSearchLots(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search lots: " + e.getMessage());
        }
    }
}
