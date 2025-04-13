package kr.cms.inventoryService.service;

import jakarta.transaction.Transactional;
import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventoryUpdateRequestDTO;
import kr.cms.inventoryService.entity.Inventory;
import kr.cms.inventoryService.entity.InventoryHistory;
import kr.cms.inventoryService.logging.LogSender;
import kr.cms.inventoryService.repository.InventoryHistoryRepository;
import kr.cms.inventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final LogSender logSender;

    @Override
    public ApiResponse<InventoryDTO> getInventory(Long inventoryId, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));
            InventoryDTO dto = convertToDTO(inventory);
            logSender.sendLog("GET_INVENTORY_SUCCESS", "Inventory retrieved successfully.", ip, userAgent, loginId);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            logSender.sendLog("GET_INVENTORY_FAIL", "Failed to retrieve inventory: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieve inventory: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<InventoryDTO>> getAllInventory(String ip, String userAgent, String loginId) {
        try {
            List<Inventory> entityList = inventoryRepository.findAll();
            List<InventoryDTO> dtoList = entityList.stream()
                            .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_INVENTORIES_SUCCESS", "All inventories retrieved successfully.", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_INVENTORIES_FAIL", "Failed to retrieved categories: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieved categories: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ApiResponse<InventoryDTO> increaseInventory(InventoryUpdateRequestDTO req, String ip, String userAgent, String loginId) {
        try {
            Long itemId = req.getItemId();
            Integer warehouseId = req.getWarehouseId();
            Integer binId = req.getBinId();
            Integer lotId = req.getLotId();
            int quantity = req.getQuantity();
            String originRef = req.getOriginRef();

            Inventory inventory = inventoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotId(itemId, warehouseId, binId, lotId)
                    .orElseGet(() -> {
                        Inventory newInv = new Inventory();
                        newInv.setItemId(itemId);
                        newInv.setWarehouseId(warehouseId);
                        newInv.setBinId(binId);
                        newInv.setLotId(lotId);
                        newInv.setCurrentQuantity(0);
                        newInv.setReservedQuantity(0);
                        newInv.setUpdatedAt(LocalDateTime.now());
                        return newInv;
                    });

            int preQuantity = inventory.getCurrentQuantity();
            inventory.setCurrentQuantity(preQuantity + quantity);
            inventory.setUpdatedAt(LocalDateTime.now());
            Inventory saved = inventoryRepository.save(inventory);

            createInventoryHistory(itemId, warehouseId, binId, lotId,
                    "INBOUND", quantity, preQuantity, saved.getCurrentQuantity(), originRef, "inbound_service");

            logSender.sendLog("INCREASE_INVENTORY_SUCCESS", "Inventory increased by " + quantity + " units.", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(saved));
        } catch (Exception e) {
            logSender.sendLog("INCREASE_INVENTORY_FAIL", "Failed to increase inventory: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to increase inventory: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ApiResponse<InventoryDTO> decreaseInventory(InventoryUpdateRequestDTO req, String ip, String userAgent, String loginId) {
        try {
            Long itemId = req.getItemId();
            Integer warehouseId = req.getWarehouseId();
            Integer binId = req.getBinId();
            Integer lotId = req.getLotId();
            int quantity = req.getQuantity();
            String originRef = req.getOriginRef();

            Inventory inventory = inventoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotId(itemId, warehouseId, binId, lotId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            int preQuantity = inventory.getCurrentQuantity();
            if (preQuantity < quantity) {
                String errMsg = "Insufficient inventory: Requested " + quantity + ", available " + preQuantity;
                logSender.sendLog("DECREASE_INVENTORY_FAIL", errMsg, ip, userAgent, loginId);
                return ApiResponse.fail(errMsg);
            }
            inventory.setCurrentQuantity(preQuantity - quantity);
            inventory.setUpdatedAt(LocalDateTime.now());
            Inventory saved = inventoryRepository.save(inventory);

            createInventoryHistory(itemId, warehouseId, binId, lotId,
                    "OUTBOUND", -quantity, preQuantity, saved.getCurrentQuantity(), originRef, "outbound_service");

            logSender.sendLog("DECREASE_INVENTORY_SUCCESS", "Inventory decreased by " + quantity + " units.", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(saved));
        } catch (Exception e) {
            logSender.sendLog("DECREASE_INVENTORY_FAIL", "Failed to decrease inventory: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to decrease inventory: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(Long itemId, Integer warehouseId, Integer binId, Integer lotId, String ip, String userAgent, String loginId) {
        try {
            List<InventoryHistoryDTO> histories = inventoryHistoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotId(itemId, warehouseId, binId, lotId)
                    .stream()
                    .map(this::convertToInventoryHistoryDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_INVENTORY_HISTORY_SUCCESS", "Retrieved " + histories.size() + " inventory history records.", ip, userAgent, loginId);
            return ApiResponse.success(histories);
        } catch (Exception e) {
            logSender.sendLog("GET_INVENTORY_HISTORY_FAIL", "Error retrieving inventory history: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail(e.getMessage());
        }
    }


    private Inventory convertToEntity(InventoryDTO dto) {
        Inventory entity = new Inventory();
        entity.setInventoryId(dto.getInventoryId());
        entity.setItemId(dto.getItemId());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setBinId(dto.getBinId());
        entity.setLotId(dto.getLotId());
        entity.setCurrentQuantity(dto.getCurrentQuantity());
        entity.setReservedQuantity(dto.getReservedQuantity());
        entity.setVersion(dto.getVersion());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    private InventoryDTO convertToDTO(Inventory entity) {
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(entity.getInventoryId());
        dto.setItemId(entity.getItemId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setBinId(entity.getBinId());
        dto.setLotId(entity.getLotId());
        dto.setCurrentQuantity(entity.getCurrentQuantity());
        dto.setReservedQuantity(entity.getReservedQuantity());
        dto.setVersion(entity.getVersion());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private void createInventoryHistory(Long itemId, Integer warehouseId, Integer binId, Integer lotId,
                                        String transactionType, int quantityChange, int preQuantity, int postQuantity,
                                        String originRef, String operator) {
        InventoryHistory history = new InventoryHistory();
        history.setItemId(itemId);
        history.setWarehouseId(warehouseId);
        history.setBinId(binId);
        history.setLotId(lotId);
        history.setTransactionType(transactionType);
        history.setQuantityChange(quantityChange);
        history.setPreQuantity(preQuantity);
        history.setPostQuantity(postQuantity);
        history.setOriginRef(originRef);
        history.setOperator(operator);
        history.setTransactionDate(LocalDateTime.now());
        inventoryHistoryRepository.save(history);
    }

    private InventoryHistoryDTO convertToInventoryHistoryDTO(InventoryHistory history) {
        return new InventoryHistoryDTO(
                history.getTransactionId(),
                history.getItemId(),
                history.getWarehouseId(),
                history.getBinId(),
                history.getLotId(),
                history.getTransactionType(),
                history.getQuantityChange(),
                history.getPreQuantity(),
                history.getPostQuantity(),
                history.getTransactionDate(),
                history.getOriginRef(),
                history.getOperator()
        );
    }
}
