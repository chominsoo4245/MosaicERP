package kr.cms.inventoryService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.inventoryService.dto.InventoryDTO;
import kr.cms.inventoryService.entity.Inventory;
import kr.cms.inventoryService.entity.InventoryHistory;
import kr.cms.inventoryService.entity.InventoryTransaction;
import kr.cms.inventoryService.logging.LogSender;
import kr.cms.inventoryService.repository.InventoryHistoryRepository;
import kr.cms.inventoryService.repository.InventoryRepository;
import kr.cms.inventoryService.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kr.cms.inventoryService.util.DataUtil.convertToDTO;
import static kr.cms.inventoryService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private InventoryTransaction getValidTransaction(String transactionId) {
        InventoryTransaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!TransactionStatus.TRYING.equals(transaction.getStatus())) {
            throw new RuntimeException("Invalid transaction state: " + transaction.getStatus());
        }

        if (transaction.getExpireAt().isBefore(LocalDateTime.now())) {
            transaction.setStatus(TransactionStatus.CANCELLED);
            transactionRepository.save(transaction);
            throw new RuntimeException("Transaction expired");
        }

        return transaction;
    }

    private void createTransaction(String transactionId, String operation, String inventoryData, String originalData) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setInventoryData(inventoryData);
        transaction.setOriginalData(originalData);
        transaction.setStatus(TransactionStatus.TRYING);
        transaction.setOperation(operation);
        LocalDateTime now = LocalDateTime.now();
        transaction.setCreatedAt(now);
        transaction.setExpireAt(now.plusMinutes(10));
        transactionRepository.save(transaction);
    }

    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }

    private void validateInventoryData(InventoryDTO inventoryDTO) {
        // 검증 로직
        if (inventoryDTO.getItemId() == null || inventoryDTO.getWarehouseId() == null) {
            throw new IllegalArgumentException("Item ID and Warehouse ID are required");
        }

        if (inventoryDTO.getCurrentQuantity() == null) {
            inventoryDTO.setCurrentQuantity(0);
        }

        if (inventoryDTO.getReservedQuantity() == null) {
            inventoryDTO.setReservedQuantity(0);
        }
    }

    @Transactional
    public ApiResponse<String> tryCreateInventory(String transactionId, InventoryDTO inventoryDTO, String ip, String userAgent, String loginId) {
        try {
            validateInventoryData(inventoryDTO);

            String inventoryData = objectMapper.writeValueAsString(inventoryDTO);
            createTransaction(transactionId, "CREATE", inventoryData, null);
            sendLog("TRY_CREATE_INVENTORY", "Inventory creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_CREATE_INVENTORY_FAIL", "Failed to prepare inventory creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare inventory creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateInventory(String transactionId, InventoryDTO updatedDTO,
                                                      String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);

            // 트랜잭션에서 기존 데이터 읽기
            InventoryDTO storedDTO = objectMapper.readValue(transaction.getInventoryData(), InventoryDTO.class);

            // updatedDTO에서 필요한 정보(itemId, lotId 등)를 storedDTO에 업데이트
            if (updatedDTO.getItemId() != null) {
                storedDTO.setItemId(updatedDTO.getItemId());
            }
            if (updatedDTO.getLotId() != null) {
                storedDTO.setLotId(updatedDTO.getLotId());
            }

            // 업데이트된 DTO를 엔티티로 변환하여 저장
            Inventory entity = convertToEntity(storedDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setInventoryId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entity.setVersion(1);
            Inventory saved = inventoryRepository.save(entity);

            // 재고 히스토리 생성
            createInventoryHistory(
                    saved.getItemId(),
                    saved.getWarehouseId(),
                    saved.getBinId(),
                    saved.getLotId(),
                    "INITIAL",
                    saved.getCurrentQuantity(),
                    0,
                    saved.getCurrentQuantity(),
                    "ITEM_CREATION",
                    loginId);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getInventoryId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_CREATE_INVENTORY", "Inventory created successfully with ID: " + saved.getInventoryId(), ip, userAgent, loginId);
            return ApiResponse.success(saved.getInventoryId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_CREATE_INVENTORY_FAIL", "Failed to confirm inventory creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm inventory creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateInventory(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_INVENTORY", "Inventory creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Inventory creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_INVENTORY_FAIL", "Failed to cancel inventory creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel inventory creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateInventory(String transactionId, InventoryDTO inventoryDTO, String ip, String userAgent, String loginId) {
        try {
            Inventory existingInventory = inventoryRepository.findById(inventoryDTO.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));
            validateInventoryData(inventoryDTO);

            InventoryDTO originalInventoryDTO = convertToDTO(existingInventory);
            String inventoryData = objectMapper.writeValueAsString(inventoryDTO);
            String originalData = objectMapper.writeValueAsString(originalInventoryDTO);
            createTransaction(transactionId, "UPDATE", inventoryData, originalData);

            sendLog("TRY_UPDATE_INVENTORY", "Inventory update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_INVENTORY_FAIL", "Failed to prepare inventory update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare inventory update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateInventory(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            InventoryDTO inventoryDTO = objectMapper.readValue(transaction.getInventoryData(), InventoryDTO.class);
            InventoryDTO originalDTO = objectMapper.readValue(transaction.getOriginalData(), InventoryDTO.class);

            Inventory entity = convertToEntity(inventoryDTO);
            entity.setVersion(entity.getVersion() + 1);
            entity.setUpdatedAt(LocalDateTime.now());
            Inventory updated = inventoryRepository.save(entity);

            // 재고 변경량 계산 및 히스토리 생성
            int quantityChange = entity.getCurrentQuantity() - originalDTO.getCurrentQuantity();
            if (quantityChange != 0) {
                String transactionType = quantityChange > 0 ? "INCREASE" : "DECREASE";
                createInventoryHistory(
                        updated.getItemId(),
                        updated.getWarehouseId(),
                        updated.getBinId(),
                        updated.getLotId(),
                        transactionType,
                        quantityChange,
                        originalDTO.getCurrentQuantity(),
                        updated.getCurrentQuantity(),
                        "INVENTORY_UPDATE",
                        loginId);
            }

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getInventoryId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_INVENTORY", "Inventory updated successfully with ID: " + updated.getInventoryId(), ip, userAgent, loginId);
            return ApiResponse.success(updated.getInventoryId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_INVENTORY_FAIL", "Failed to confirm inventory update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm inventory update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateInventory(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_INVENTORY", "Inventory update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Inventory update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_INVENTORY_FAIL", "Failed to cancel inventory update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel inventory update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryIncreaseInventory(String transactionId, Long inventoryId, Integer quantity, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            InventoryDTO originalDTO = convertToDTO(inventory);
            InventoryDTO updatedDTO = convertToDTO(inventory);
            updatedDTO.setCurrentQuantity(updatedDTO.getCurrentQuantity() + quantity);

            String inventoryData = objectMapper.writeValueAsString(updatedDTO);
            String originalData = objectMapper.writeValueAsString(originalDTO);

            // 메타데이터(수량 변경량)만 저장
            InventoryDTO metadataDTO = new InventoryDTO();
            metadataDTO.setCurrentQuantity(quantity);

            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setTransactionId(transactionId);
            transaction.setInventoryData(inventoryData);
            transaction.setOriginalData(originalData);
            transaction.setStatus(TransactionStatus.TRYING);
            transaction.setOperation("INCREASE");
            transaction.setMetadata(objectMapper.writeValueAsString(metadataDTO));

            LocalDateTime now = LocalDateTime.now();
            transaction.setCreatedAt(now);
            transaction.setExpireAt(now.plusMinutes(10));
            transactionRepository.save(transaction);

            sendLog("TRY_INCREASE_INVENTORY", "Inventory increase prepared for ID: " + inventoryId + " by " + quantity, ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_INCREASE_INVENTORY_FAIL", "Failed to prepare inventory increase: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare inventory increase: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmIncreaseInventory(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            InventoryDTO inventoryDTO = objectMapper.readValue(transaction.getInventoryData(), InventoryDTO.class);
            InventoryDTO originalDTO = objectMapper.readValue(transaction.getOriginalData(), InventoryDTO.class);
            InventoryDTO metadataDTO = objectMapper.readValue(transaction.getMetadata(), InventoryDTO.class);
            Integer quantityChange = metadataDTO.getCurrentQuantity();

            Inventory entity = convertToEntity(inventoryDTO);
            entity.setVersion(entity.getVersion() + 1);
            entity.setUpdatedAt(LocalDateTime.now());
            Inventory updated = inventoryRepository.save(entity);

            // 재고 히스토리 생성
            createInventoryHistory(
                    updated.getItemId(),
                    updated.getWarehouseId(),
                    updated.getBinId(),
                    updated.getLotId(),
                    "INBOUND",
                    quantityChange,
                    originalDTO.getCurrentQuantity(),
                    updated.getCurrentQuantity(),
                    "INVENTORY_INCREASE",
                    loginId);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getInventoryId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_INCREASE_INVENTORY", "Inventory increased successfully for ID: " + updated.getInventoryId(), ip, userAgent, loginId);
            return ApiResponse.success(updated.getInventoryId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_INCREASE_INVENTORY_FAIL", "Failed to confirm inventory increase: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm inventory increase: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDecreaseInventory(String transactionId, Long inventoryId, Integer quantity, String ip, String userAgent, String loginId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            // 재고 확인
            if (inventory.getCurrentQuantity() < quantity) {
                throw new RuntimeException("Insufficient inventory. Available: " + inventory.getCurrentQuantity() + ", Requested: " + quantity);
            }

            InventoryDTO originalDTO = convertToDTO(inventory);
            InventoryDTO updatedDTO = convertToDTO(inventory);
            updatedDTO.setCurrentQuantity(updatedDTO.getCurrentQuantity() - quantity);

            String inventoryData = objectMapper.writeValueAsString(updatedDTO);
            String originalData = objectMapper.writeValueAsString(originalDTO);

            InventoryDTO metadataDTO = new InventoryDTO();
            metadataDTO.setCurrentQuantity(quantity);

            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setTransactionId(transactionId);
            transaction.setInventoryData(inventoryData);
            transaction.setOriginalData(originalData);
            transaction.setStatus(TransactionStatus.TRYING);
            transaction.setOperation("DECREASE");
            transaction.setMetadata(objectMapper.writeValueAsString(metadataDTO));

            LocalDateTime now = LocalDateTime.now();
            transaction.setCreatedAt(now);
            transaction.setExpireAt(now.plusMinutes(10));
            transactionRepository.save(transaction);

            sendLog("TRY_DECREASE_INVENTORY", "Inventory decrease prepared for ID: " + inventoryId + " by " + quantity, ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DECREASE_INVENTORY_FAIL", "Failed to prepare inventory decrease: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare inventory decrease: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDecreaseInventory(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            InventoryDTO inventoryDTO = objectMapper.readValue(transaction.getInventoryData(), InventoryDTO.class);
            InventoryDTO originalDTO = objectMapper.readValue(transaction.getOriginalData(), InventoryDTO.class);
            InventoryDTO metadataDTO = objectMapper.readValue(transaction.getMetadata(), InventoryDTO.class);
            Integer quantityChange = metadataDTO.getCurrentQuantity();

            Inventory entity = convertToEntity(inventoryDTO);
            entity.setVersion(entity.getVersion() + 1);
            entity.setUpdatedAt(LocalDateTime.now());
            Inventory updated = inventoryRepository.save(entity);

            // 재고 히스토리 생성
            createInventoryHistory(
                    updated.getItemId(),
                    updated.getWarehouseId(),
                    updated.getBinId(),
                    updated.getLotId(),
                    "OUTBOUND",
                    -quantityChange,  // 감소니까 음수
                    originalDTO.getCurrentQuantity(),
                    updated.getCurrentQuantity(),
                    "INVENTORY_DECREASE",
                    loginId);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getInventoryId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DECREASE_INVENTORY", "Inventory decreased successfully for ID: " + updated.getInventoryId(), ip, userAgent, loginId);
            return ApiResponse.success(updated.getInventoryId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_DECREASE_INVENTORY_FAIL", "Failed to confirm inventory decrease: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm inventory decrease: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelInventoryOperation(String transactionId, String ip, String userAgent, String loginId) {
        try {
            InventoryTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_INVENTORY_OPERATION", "Inventory operation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Inventory operation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_INVENTORY_OPERATION_FAIL", "Failed to cancel inventory operation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel inventory operation: " + e.getMessage(), e);
        }
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
        history.setCreatedAt(LocalDateTime.now());
        history.setUpdatedAt(LocalDateTime.now());
        inventoryHistoryRepository.save(history);
    }
}