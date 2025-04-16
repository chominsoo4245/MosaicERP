package kr.cms.serialService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.dto.SearchDataDTO;

import java.util.List;

public interface SerialService {
    ApiResponse<SerialDTO> getSerial(Long lotId, String ip, String userAgent, String loginId);

    ApiResponse<List<SerialDTO>> getAllSerial(String ip, String userAgent, String loginId);

    ApiResponse<List<SerialDTO>> getSearchSerials(SearchDataDTO dto, String ip, String userAgent, String loginId);
}
