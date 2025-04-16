package kr.cms.supplierService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.supplierService.dto.SearchDataDTO;
import kr.cms.supplierService.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {
    ApiResponse<SupplierDTO> getSupplier(Long supplierId, String ip, String userAgent, String loginId);
    ApiResponse<List<SupplierDTO>> getAllSupplier(String ip, String userAgent, String loginId);
    ApiResponse<List<SupplierDTO>> getSearchSuppliers(SearchDataDTO dto, String ip, String userAgent, String loginId);
}
