package kr.cms.supplierService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.supplierService.dto.SearchDataDTO;
import kr.cms.supplierService.dto.SupplierDTO;
import kr.cms.supplierService.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier-service")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<SupplierDTO> getSupplier(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.getSupplier(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve supplier: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<SupplierDTO>> getSupplierList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.getAllSupplier(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve suppliers: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<SupplierDTO>> getSearchSupplier(@RequestBody SearchDataDTO searchDataDTO){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.getSearchSuppliers(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search suppliers: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ApiResponse<Long> addSupplier(@RequestBody SupplierDTO dto){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.createSupplier(dto, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to created supplier: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ApiResponse<String> editSupplier(@RequestBody SupplierDTO dto) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.updateSupplier(dto, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to update supplier: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeSupplier(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return supplierService.deleteSupplier(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to delete supplier: " + e.getMessage());
        }
    }
}
