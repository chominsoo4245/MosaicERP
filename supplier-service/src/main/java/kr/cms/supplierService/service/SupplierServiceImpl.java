package kr.cms.supplierService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.common.dto.ApiResponse;
import kr.cms.supplierService.dto.SearchDataDTO;
import kr.cms.supplierService.dto.SupplierDTO;
import kr.cms.supplierService.entity.Supplier;
import kr.cms.supplierService.logging.LogSender;
import kr.cms.supplierService.repository.SupplierRepository;
import kr.cms.supplierService.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.supplierService.util.DataUtil.convertToDTO;
import static kr.cms.supplierService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final LogSender logSender;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<SupplierDTO> getSupplier(Long supplierId, String ip, String userAgent, String loginId) {
        try {
            Supplier entity = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            logSender.sendLog("GET_SUPPLIER_SUCCESS", "Supplier retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_SUPPLIER_FAIL", "Failed to retrieve supplier: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<SupplierDTO>> getAllSupplier(String ip, String userAgent, String loginId) {
        try {
            List<Supplier> entity = supplierRepository.findAll();
            List<SupplierDTO> dtoList = entity.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_SUPPLIER_SUCCESS", "All suppliers retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_SUPPLIER_FAIL", "Failed to retrieve suppliers: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<SupplierDTO>> getSearchSuppliers(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO))
            return getAllSupplier(ip, userAgent, loginId);
        try {
            Specification<Supplier> spec = createSupplierSpecification(searchDataDTO);
            List<Supplier> entityList = supplierRepository.findAll(spec);
            List<SupplierDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_SUPPLIERS_SUCCESS", "Suppliers retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_SUPPLIERS_FAIL", "Failed to search suppliers: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }


    private Specification<Supplier> createSupplierSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("name"), keyword),
                        cb.like(root.get("contact_details"), keyword),
                        cb.like(root.get("address"), keyword)
                ));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty());
    }
}
