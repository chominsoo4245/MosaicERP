package kr.cms.serialService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.common.dto.ApiResponse;
import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.dto.SearchDataDTO;
import kr.cms.serialService.entity.Serial;
import kr.cms.serialService.logging.LogSender;
import kr.cms.serialService.repository.SerialRepository;
import kr.cms.serialService.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.serialService.util.DataUtil.convertToDTO;

@Service
@RequiredArgsConstructor
public class SerialServiceImpl implements SerialService {
    
    private final SerialRepository serialRepository;
    private final LogSender logSender;
    
    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<SerialDTO> getSerial(Long serialId, String ip, String userAgent, String loginId) {
        try {
            Serial entity = serialRepository.findById(serialId)
                    .orElseThrow(() -> new RuntimeException("Serial not found"));
            logSender.sendLog("GET_SUPPLIER_SUCCESS", "Serial retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_SUPPLIER_FAIL", "Failed to retrieve serial: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<SerialDTO>> getAllSerial(String ip, String userAgent, String loginId) {
        try {
            List<Serial> entity = serialRepository.findAll();
            List<SerialDTO> dtoList = entity.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_SUPPLIER_SUCCESS", "All serials retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_SUPPLIER_FAIL", "Failed to retrieve serials: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<SerialDTO>> getSearchSerials(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO))
            return getAllSerial(ip, userAgent, loginId);
        try {
            Specification<Serial> spec = createSerialSpecification(searchDataDTO);
            List<Serial> entityList = serialRepository.findAll(spec);
            List<SerialDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_SUPPLIERS_SUCCESS", "Serials retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_SUPPLIERS_FAIL", "Failed to search serials: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }


    private Specification<Serial> createSerialSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("serial_number"), keyword)
                ));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty());
    }
}
