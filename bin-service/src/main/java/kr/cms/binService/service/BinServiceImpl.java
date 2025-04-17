package kr.cms.binService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.entity.Bin;
import kr.cms.binService.util.DataUtil;
import kr.cms.common.dto.ApiResponse;
import kr.cms.binService.dto.SearchDataDTO;
import kr.cms.binService.logging.LogSender;
import kr.cms.binService.repository.BinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BinServiceImpl implements BinService {
    
    private final BinRepository binRepository;
    private final LogSender logSender;
    
    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<BinDTO> getBin(Long binId, String ip, String userAgent, String loginId) {
        try {
            Bin entity = binRepository.findById(binId)
                    .orElseThrow(() -> new RuntimeException("Bin not found"));
            logSender.sendLog("GET_SUPPLIER_SUCCESS", "Bin retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(DataUtil.convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_SUPPLIER_FAIL", "Failed to retrieve bin: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BinDTO>> getAllBin(String ip, String userAgent, String loginId) {
        try {
            List<Bin> entity = binRepository.findAll();
            List<BinDTO> dtoList = entity.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_SUPPLIER_SUCCESS", "All bins retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_SUPPLIER_FAIL", "Failed to retrieve bins: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BinDTO>> getSearchBins(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO))
            return getAllBin(ip, userAgent, loginId);
        try {
            Specification<Bin> spec = createBinSpecification(searchDataDTO);
            List<Bin> entityList = binRepository.findAll(spec);
            List<BinDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_SUPPLIERS_SUCCESS", "Bins retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_SUPPLIERS_FAIL", "Failed to search bins: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }


    private Specification<Bin> createBinSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("name"), keyword),
                        cb.like(root.get("description"), keyword)
                ));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty());
    }
}
