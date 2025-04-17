package kr.cms.warehouseService.service;

import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.dto.SearchDataDTO;
import kr.cms.common.dto.ApiResponse;

import java.util.List;

public interface WarehouseService {
    ApiResponse<WarehouseDTO> getWarehouse(Long lotId, String ip, String userAgent, String loginId);

    ApiResponse<List<WarehouseDTO>> getAllWarehouse(String ip, String userAgent, String loginId);

    ApiResponse<List<WarehouseDTO>> getSearchWarehouses(SearchDataDTO dto, String ip, String userAgent, String loginId);
}
