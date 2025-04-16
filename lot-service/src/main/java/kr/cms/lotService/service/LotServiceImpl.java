package kr.cms.lotService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.common.dto.ApiResponse;
import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.dto.SearchDataDTO;
import kr.cms.lotService.entity.Lot;
import kr.cms.lotService.logging.LogSender;
import kr.cms.lotService.repository.LotRepository;
import kr.cms.lotService.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.lotService.util.DataUtil.convertToDTO;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService{
    
    private final LotRepository lotRepository;
    private final LogSender logSender;
    
    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<LotDTO> getLot(Long lotId, String ip, String userAgent, String loginId) {
        try {
            Lot entity = lotRepository.findById(lotId)
                    .orElseThrow(() -> new RuntimeException("Lot not found"));
            logSender.sendLog("GET_SUPPLIER_SUCCESS", "Lot retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_SUPPLIER_FAIL", "Failed to retrieve lot: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<LotDTO>> getAllLot(String ip, String userAgent, String loginId) {
        try {
            List<Lot> entity = lotRepository.findAll();
            List<LotDTO> dtoList = entity.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_SUPPLIER_SUCCESS", "All lots retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_SUPPLIER_FAIL", "Failed to retrieve lots: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<LotDTO>> getSearchLots(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO))
            return getAllLot(ip, userAgent, loginId);
        try {
            Specification<Lot> spec = createLotSpecification(searchDataDTO);
            List<Lot> entityList = lotRepository.findAll(spec);
            List<LotDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_SUPPLIERS_SUCCESS", "Lots retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_SUPPLIERS_FAIL", "Failed to search lots: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }


    private Specification<Lot> createLotSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("lot_number"), keyword),
                        cb.like(root.get("location_info"), keyword)
                ));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty());
    }
}
