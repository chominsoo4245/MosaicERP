package kr.cms.warehouseService.controller;

import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.dto.SearchDataDTO;
import kr.cms.warehouseService.service.WarehouseService;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse-service")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<WarehouseDTO> getWarehouse(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return warehouseService.getWarehouse(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve warehouse: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<WarehouseDTO>> getWarehouseList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return warehouseService.getAllWarehouse(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve warehouses: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<WarehouseDTO>> getSearchWarehouse(@RequestBody SearchDataDTO searchDataDTO){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return warehouseService.getSearchWarehouses(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search warehouses: " + e.getMessage());
        }
    }
}
