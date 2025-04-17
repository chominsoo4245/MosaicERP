package kr.cms.itemService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.entity.Item;
import kr.cms.itemService.entity.ItemTransaction;
import kr.cms.itemService.logging.LogSender;
import kr.cms.itemService.repository.ItemRepository;
import kr.cms.itemService.repository.ItemTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.cms.itemService.util.DataUtil.convertToDTO;
import static kr.cms.itemService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class ItemTransactionService {

    private final ItemTransactionRepository transactionRepository;
    private final ItemRepository itemRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private ItemTransaction getValidTransaction(String transactionId) {
        ItemTransaction transaction = transactionRepository.findByTransactionId(transactionId)
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

    private void createTransaction(String transactionId, String operation, String itemData, String originalData) {
        ItemTransaction transaction = new ItemTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setItemData(itemData);
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

    private void validateItemData(ItemDTO itemDTO){

    }
    
    @Transactional
    public ApiResponse<String> tryCreateItem(String transactionId, ItemDTO itemDTO, String ip, String userAgent, String loginId){
        try {

            validateItemData(itemDTO);

            String itemData = objectMapper.writeValueAsString(itemDTO);
            createTransaction(transactionId, "CREATE", itemData, null);
            sendLog("TRY_CREATE_ITEM", "Item creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_CREATE_ITEM_FAIL", "Failed to prepare item creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare item creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);

            ItemDTO itemDTO = objectMapper.readValue(transaction.getItemData(), ItemDTO.class);
            Item entity = convertToEntity(itemDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setItemId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Item saved = itemRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getItemId().toString());
            transactionRepository.save(transaction);
            sendLog("CONFIRM_CREATE_ITEM", "Item created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getItemId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_CREATE_ITEM_FAIL", "Failed to confirm item creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm item creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_ITEM", "Item creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_ITEM_FAIL", "Failed to cancel item creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel item creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateItem(String transactionId, ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        try {
            Item existingItem = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            validateItemData(itemDTO);

            ItemDTO originalItemDTO = convertToDTO(existingItem);
            String itemData = objectMapper.writeValueAsString(itemDTO);
            String originalData = objectMapper.writeValueAsString(originalItemDTO);
            createTransaction(transactionId, "UPDATE", itemData, originalData);

            sendLog("TRY_UPDATE_ITEM", "Item update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_ITEM_FAIL", "Failed to prepare item update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare item update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);
            ItemDTO itemDTO = objectMapper.readValue(transaction.getItemData(), ItemDTO.class);
            Item entity = convertToEntity(itemDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            Item updated = itemRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getItemId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_ITEM", "Item updated successfully", ip, userAgent, loginId);
            return ApiResponse.success(updated.getItemId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_ITEM_FAIL", "Failed to confirm item update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm item update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_ITEM", "Item update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_ITEM_FAIL", "Failed to cancel item update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel item update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteItem(String transactionId, Long itemId, String ip, String userAgent, String loginId) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            String itemData = objectMapper.writeValueAsString(convertToDTO(item));
            createTransaction(transactionId, "DELETE", itemData, null);
            sendLog("TRY_DELETE_ITEM", "Item deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_ITEM_FAIL", "Failed to prepare item deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare item deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);
            ItemDTO itemDTO = objectMapper.readValue(transaction.getItemData(), ItemDTO.class);
            itemRepository.deleteById(itemDTO.getItemId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_ITEM", "Item deleted successfully", ip, userAgent, loginId);
            return ApiResponse.success(itemDTO.getItemId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_ITEM_FAIL", "Failed to confirm item deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm item deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteItem(String transactionId, String ip, String userAgent, String loginId) {
        try {
            ItemTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_ITEM", "Item deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_ITEM_FAIL", "Failed to cancel item deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel item deletion: " + e.getMessage(), e);
        }
    }
}
