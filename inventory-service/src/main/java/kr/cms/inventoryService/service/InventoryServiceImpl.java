package kr.cms.inventoryService.service;

import jakarta.transaction.Transactional;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.AuditLogDto;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.dto.InventoryHistoryDTO;
import kr.cms.inventoryService.dto.InventorySearchParamDTO;
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
    public ApiResponse<InventoryDTO> getInventory(Long itemId, Integer warehouseId, Integer binId, String lotNumber, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotNumber(itemId, warehouseId, binId, lotNumber)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));
            InventoryDTO dto = convertToInventoryDTO(inventory);
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_SUCCESS",
                    loginId,
                    "Inventory retrieved successfully.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(dto);
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_FAIL",
                    loginId,
                    "Error retrieving inventory: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    @Override
    public ApiResponse<InventoryDTO> getInventoryDetail(Long inventoryId, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));
            InventoryDTO dto = convertToInventoryDTO(inventory);
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_DETAIL_SUCCESS",
                    loginId,
                    "Inventory detail retrieved successfully.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(dto);
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_DETAIL_FAIL",
                    loginId,
                    "Error retrieving inventory detail: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<InventoryDTO>> searchInventory(InventorySearchParamDTO searchParam, String ip, String userAgent, String loginId) {
        try {
            List<Inventory> list = inventoryRepository.searchByCriteria(searchParam);

            List<InventoryDTO> dtos = list.stream()
                    .map(this::convertToInventoryDTO)
                    .collect(Collectors.toList());
            logSender.sendAuditLog(new AuditLogDto(
                    "SEARCH_INVENTORY_SUCCESS",
                    loginId,
                    "Retrieved " + dtos.size() + " inventory records based on search criteria.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(dtos);
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "SEARCH_INVENTORY_FAIL",
                    loginId,
                    "Error searching inventory: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ApiResponse<InventoryDTO> increaseInventory(InventoryUpdateRequestDTO req, String ip, String userAgent, String loginId) {
        try {
            Long itemId = req.getItemId();
            Integer warehouseId = req.getWarehouseId();
            Integer binId = req.getBinId();
            String lotNumber = req.getLotNumber();
            LocalDateTime expirationDate = req.getExpirationDate();
            int quantity = req.getQuantity();
            String originRef = req.getOriginRef();

            Inventory inventory = inventoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotNumber(itemId, warehouseId, binId, lotNumber)
                    .orElseGet(() -> {
                        Inventory newInv = new Inventory();
                        newInv.setItemId(itemId);
                        newInv.setWarehouseId(warehouseId);
                        newInv.setBinId(binId);
                        newInv.setLotNumber(lotNumber);
                        newInv.setExpirationDate(expirationDate);
                        newInv.setCurrentQuantity(0);
                        newInv.setReservedQuantity(0);
                        newInv.setUpdatedAt(LocalDateTime.now());
                        return newInv;
                    });

            int preQuantity = inventory.getCurrentQuantity();
            inventory.setCurrentQuantity(preQuantity + quantity);
            inventory.setUpdatedAt(LocalDateTime.now());
            Inventory saved = inventoryRepository.save(inventory);

            createInventoryHistory(itemId, warehouseId, binId, lotNumber, expirationDate,
                    "INBOUND", quantity, preQuantity, saved.getCurrentQuantity(), originRef, "inbound_service");

            logSender.sendAuditLog(new AuditLogDto(
                    "INCREASE_INVENTORY_SUCCESS",
                    loginId,
                    "Inventory increased by " + quantity + " units.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(convertToInventoryDTO(saved));
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "INCREASE_INVENTORY_FAIL",
                    loginId,
                    "Error increasing inventory: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ApiResponse<InventoryDTO> decreaseInventory(InventoryUpdateRequestDTO req, String ip, String userAgent, String loginId) {
        try {
            Long itemId = req.getItemId();
            Integer warehouseId = req.getWarehouseId();
            Integer binId = req.getBinId();
            String lotNumber = req.getLotNumber();
            LocalDateTime expirationDate = req.getExpirationDate();
            int quantity = req.getQuantity();
            String originRef = req.getOriginRef();

            Inventory inventory = inventoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotNumber(itemId, warehouseId, binId, lotNumber)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            int preQuantity = inventory.getCurrentQuantity();
            if (preQuantity < quantity) {
                String errMsg = "Insufficient inventory: Requested " + quantity + ", available " + preQuantity;
                logSender.sendAuditLog(new AuditLogDto(
                        "DECREASE_INVENTORY_FAIL",
                        loginId,
                        errMsg,
                        ip,
                        userAgent,
                        LocalDateTime.now()
                ));
                return ApiResponse.fail(errMsg);
            }
            inventory.setCurrentQuantity(preQuantity - quantity);
            inventory.setUpdatedAt(LocalDateTime.now());
            Inventory saved = inventoryRepository.save(inventory);

            createInventoryHistory(itemId, warehouseId, binId, lotNumber, expirationDate,
                    "OUTBOUND", -quantity, preQuantity, saved.getCurrentQuantity(), originRef, "outbound_service");

            logSender.sendAuditLog(new AuditLogDto(
                    "DECREASE_INVENTORY_SUCCESS",
                    loginId,
                    "Inventory decreased by " + quantity + " units.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(convertToInventoryDTO(saved));
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "DECREASE_INVENTORY_FAIL",
                    loginId,
                    "Error decreasing inventory: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<InventoryHistoryDTO>> getInventoryHistory(Long itemId, Integer warehouseId, Integer binId, String lotNumber, String ip, String userAgent, String loginId) {
        try {
            List<InventoryHistoryDTO> histories = inventoryHistoryRepository.findByItemIdAndWarehouseIdAndBinIdAndLotNumber(itemId, warehouseId, binId, lotNumber)
                    .stream()
                    .map(this::convertToInventoryHistoryDTO)
                    .collect(Collectors.toList());
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_HISTORY_SUCCESS",
                    loginId,
                    "Retrieved " + histories.size() + " inventory history records.",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(histories);
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDto(
                    "GET_INVENTORY_HISTORY_FAIL",
                    loginId,
                    "Error retrieving inventory history: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail(e.getMessage());
        }
    }

    private InventoryDTO convertToInventoryDTO(Inventory inventory) {
        return new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getItemId(),
                inventory.getWarehouseId(),
                inventory.getBinId(),
                inventory.getLotNumber(),
                inventory.getExpirationDate(),
                inventory.getCurrentQuantity(),
                inventory.getReservedQuantity(),
                inventory.getVersion(),
                inventory.getCreatedAt(),
                inventory.getUpdatedAt()
        );
    }

    private void createInventoryHistory(Long itemId, Integer warehouseId, Integer binId, String lotNumber, LocalDateTime expirationDate,
                                        String transactionType, int quantityChange, int preQuantity, int postQuantity,
                                        String originRef, String operator) {
        InventoryHistory history = new InventoryHistory();
        history.setItemId(itemId);
        history.setWarehouseId(warehouseId);
        history.setBinId(binId);
        history.setLotNumber(lotNumber);
        history.setExpirationDate(expirationDate);
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
                history.getLotNumber(),
                history.getExpirationDate(),
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
