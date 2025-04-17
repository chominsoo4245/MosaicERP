package kr.cms.warehouseService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.dto.SearchDataDTO;
import kr.cms.warehouseService.entity.Warehouse;
import kr.cms.warehouseService.logging.LogSender;
import kr.cms.warehouseService.repository.WarehouseRepository;
import kr.cms.warehouseService.util.DataUtil;
import kr.cms.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.warehouseService.util.DataUtil.convertToDTO;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final LogSender logSender;
    
    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<WarehouseDTO> getWarehouse(Long warehouseId, String ip, String userAgent, String loginId) {
        try {
            Warehouse entity = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            logSender.sendLog("GET_SUPPLIER_SUCCESS", "Warehouse retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_SUPPLIER_FAIL", "Failed to retrieve warehouse: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<WarehouseDTO>> getAllWarehouse(String ip, String userAgent, String loginId) {
        try {
            List<Warehouse> entity = warehouseRepository.findAll();
            List<WarehouseDTO> dtoList = entity.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_SUPPLIER_SUCCESS", "All warehouses retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_SUPPLIER_FAIL", "Failed to retrieve warehouses: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<WarehouseDTO>> getSearchWarehouses(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO))
            return getAllWarehouse(ip, userAgent, loginId);
        try {
            Specification<Warehouse> spec = createWarehouseSpecification(searchDataDTO);
            List<Warehouse> entityList = warehouseRepository.findAll(spec);
            List<WarehouseDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_SUPPLIERS_SUCCESS", "Warehouses retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_SUPPLIERS_FAIL", "Failed to search warehouses: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }


    private Specification<Warehouse> createWarehouseSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("name"), keyword),
                        cb.like(root.get("address_line1"), keyword),
                        cb.like(root.get("address_line2"), keyword),
                        cb.like(root.get("city"), keyword),
                        cb.like(root.get("state"), keyword),
                        cb.like(root.get("zipcode"), keyword),
                        cb.like(root.get("country"), keyword)
                ));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty());
    }
}
