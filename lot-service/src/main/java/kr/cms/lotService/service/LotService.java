package kr.cms.lotService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.dto.SearchDataDTO;

import java.util.List;

public interface LotService {
    ApiResponse<LotDTO> getLot(Long lotId, String ip, String userAgent, String loginId);

    ApiResponse<List<LotDTO>> getAllLot(String ip, String userAgent, String loginId);

    ApiResponse<List<LotDTO>> getSearchLots(SearchDataDTO dto, String ip, String userAgent, String loginId);
}
