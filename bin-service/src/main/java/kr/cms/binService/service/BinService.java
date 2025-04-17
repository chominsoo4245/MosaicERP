package kr.cms.binService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.dto.SearchDataDTO;

import java.util.List;

public interface BinService {
    ApiResponse<BinDTO> getBin(Long lotId, String ip, String userAgent, String loginId);

    ApiResponse<List<BinDTO>> getAllBin(String ip, String userAgent, String loginId);

    ApiResponse<List<BinDTO>> getSearchBins(SearchDataDTO dto, String ip, String userAgent, String loginId);
}
