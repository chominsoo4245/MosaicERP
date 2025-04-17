package kr.cms.inventoryService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;
import kr.cms.inventoryService.dto.SearchDataDTO;
import kr.cms.inventoryService.entity.Inventory;
import kr.cms.inventoryService.entity.InventoryHistory;
import kr.cms.inventoryService.logging.LogSender;
import kr.cms.inventoryService.repository.InventoryHistoryRepository;
import kr.cms.inventoryService.repository.InventoryRepository;
import kr.cms.inventoryService.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.inventoryService.util.DataUtil.convertToDTO;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final LogSender logSender;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<InventoryDTO> getInventory(Long inventoryId, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));
            InventoryDTO dto = convertToDTO(inventory);
            logSender.sendLog("GET_INVENTORY_SUCCESS", "Inventory retrieved successfully.", ip, userAgent, loginId);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            logSender.sendLog("GET_INVENTORY_FAIL", "Failed to retrieve inventory: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<InventoryDTO>> getAllInventory(String ip, String userAgent, String loginId) {
        try {
            List<Inventory> entityList = inventoryRepository.findAll();
            List<InventoryDTO> dtoList = entityList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_INVENTORIES_SUCCESS", "All inventories retrieved successfully.", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_INVENTORIES_FAIL", "Failed to retrieved categories: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<InventoryDTO>> getSearchInventories(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO)) {
            return getAllInventory(ip, userAgent, loginId);
        }

        try {
            Specification<Inventory> spec = createInventorySpecification(searchDataDTO);
            List<Inventory> inventoryList = inventoryRepository.findAll(spec);
            List<InventoryDTO> dtoList = inventoryList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_INVENTORIES_SUCCESS", "Inventories retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_INVENTORIES_FAIL", "Failed to search inventories: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(Long itemId, Integer warehouseId, Integer binId, Integer lotId, String ip, String userAgent, String loginId) {
        try {
            List<InventoryHistoryDTO> histories = inventoryHistoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotId(itemId, warehouseId, binId, lotId)
                    .stream()
                    .map(DataUtil::convertToInventoryHistoryDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_INVENTORY_HISTORY_SUCCESS", "Retrieved " + histories.size() + " inventory history records.", ip, userAgent, loginId);
            return ApiResponse.success(histories);
        } catch (Exception e) {
            logSender.sendLog("GET_INVENTORY_HISTORY_FAIL", "Error retrieving inventory history: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    private Specification<Inventory> createInventorySpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getMinQuantity() != null && searchDataDTO.getMaxQuantity() != null) {
                predicate = cb.and(predicate, cb.between(
                        root.get("current_quantity"),
                        searchDataDTO.getMinQuantity(),
                        searchDataDTO.getMaxQuantity()
                ));
            } else if (searchDataDTO.getMinQuantity() != null) {
                predicate = cb.and(predicate, cb.ge(
                        root.get("current_quantity"),
                        searchDataDTO.getMinQuantity()
                ));
            } else if (searchDataDTO.getMaxQuantity() != null) {
                predicate = cb.and(predicate, cb.le(
                        root.get("current_quantity"),
                        searchDataDTO.getMaxQuantity()
                ));
            }

            if (searchDataDTO.getCreateStart() != null) {
                LocalDateTime createEnd = (searchDataDTO.getCreateEnd() != null) ? searchDataDTO.getCreateEnd() : LocalDateTime.now();
                predicate = cb.and(predicate, cb.between(root.get("created_at"), searchDataDTO.getCreateStart(), createEnd));
            }

            if (searchDataDTO.getUpdateStart() != null) {
                LocalDateTime updateEnd = (searchDataDTO.getUpdateEnd() != null) ? searchDataDTO.getUpdateEnd() : LocalDateTime.now();
                predicate = cb.and(predicate, cb.between(root.get("updated_at"), searchDataDTO.getUpdateStart(), updateEnd));
            }

            if (searchDataDTO.getItemId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("item_id"), searchDataDTO.getItemId()));
            }

            if (searchDataDTO.getWarehouseId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("warehouse_id"), searchDataDTO.getWarehouseId()));
            }

            if (searchDataDTO.getBinId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("bin_id"), searchDataDTO.getBinId()));
            }

            if (searchDataDTO.getLotId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("lot_id"), searchDataDTO.getLotId()));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return dto.getCreateStart() == null
                && dto.getCreateEnd() == null
                && dto.getUpdateStart() == null
                && dto.getUpdateEnd() == null
                && dto.getItemId() == null
                && dto.getWarehouseId() == null
                && dto.getBinId() == null
                && dto.getLotId() == null
                && dto.getMinQuantity() == null
                && dto.getMaxQuantity() == null
                && dto.getMinReserved() == null
                && dto.getMaxReserved() == null;
    }
}
