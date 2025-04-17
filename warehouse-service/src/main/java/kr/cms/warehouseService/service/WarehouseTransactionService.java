package kr.cms.warehouseService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.warehouseService.dto.WarehouseDTO;
import kr.cms.warehouseService.entity.Warehouse;
import kr.cms.warehouseService.entity.WarehouseTransaction;
import kr.cms.warehouseService.logging.LogSender;
import kr.cms.warehouseService.repository.WarehouseRepository;
import kr.cms.warehouseService.repository.WarehouseTransactionRepository;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.cms.warehouseService.util.DataUtil.convertToDTO;
import static kr.cms.warehouseService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class WarehouseTransactionService {
    private final WarehouseTransactionRepository transactionRepository;
    private final WarehouseRepository lotRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private WarehouseTransaction getValidTransaction(String transactionId) {
        WarehouseTransaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if(!TransactionStatus.TRYING.equals(transaction.getStatus())) {
            throw new RuntimeException("Invalid transaction state: " + transaction.getStatus());
        }

        if(transaction.getExpireAt().isBefore(LocalDateTime.now())) {
            transaction.setStatus(TransactionStatus.CANCELLED);
            transactionRepository.save(transaction);
            throw new RuntimeException("Transaction expired");
        }

        return transaction;
    }

    private void createTransaction(String transactionId, String operation, String lotData, String originalData) {
        WarehouseTransaction transaction = new WarehouseTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setWarehouseData(lotData);
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

    private void validateWarehouseData(WarehouseDTO lotDTO){

    }

    @Transactional
    public ApiResponse<String> tryCreateWarehouse(String transactionId, WarehouseDTO lotDTO, String ip, String userAgent, String loginId){
        try{
            validateWarehouseData(lotDTO);
            String lotData = objectMapper.writeValueAsString(lotDTO);
            createTransaction(transactionId, "CREATE", lotData, null);
            sendLog("TRY_CREATE_SUPPLIER", "lot creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        }catch (Exception e){
            sendLog("TRY_CREATE_SUPPLIER_FAIL", "Failed to prepare lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateWarehouse(String transactionId, String ip, String userAgent, String loginId){
        try{
            WarehouseTransaction transaction = getValidTransaction(transactionId);

            WarehouseDTO lotDTO = objectMapper.readValue(transaction.getWarehouseData(), WarehouseDTO.class);
            Warehouse entity = convertToEntity(lotDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setWarehouseId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Warehouse saved = lotRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getWarehouseId().toString());
            transactionRepository.save(transaction);
            sendLog("CONFIRM_CREATE_SUPPLIER", "Warehouse created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getWarehouseId().toString());
        }catch (Exception e){
            sendLog("CONFIRM_CREATE_SUPPLIER_FAIL", "Failed to confirm lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateWarehouse(String transactionId, String ip, String userAgent, String loginId) {
        try {
            WarehouseTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_SUPPLIER", "Warehouse creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Warehouse creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_SUPPLIER_FAIL", "Failed to cancel lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateWarehouse(String transactionId, WarehouseDTO lotDTO, String ip, String userAgent, String loginId) {
        try {
            Warehouse existingWarehouse = lotRepository.findById(lotDTO.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            validateWarehouseData(lotDTO);

            WarehouseDTO originalWarehouseDTO = convertToDTO(existingWarehouse);
            String lotData = objectMapper.writeValueAsString(lotDTO);
            String originalData = objectMapper.writeValueAsString(originalWarehouseDTO);
            createTransaction(transactionId, "UPDATE", lotData, originalData);

            sendLog("TRY_UPDATE_SUPPLIER", "Warehouse update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_SUPPLIER_FAIL", "Failed to prepare lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateWarehouse(String transactionId, String ip, String userAgent, String loginId) {
        try {
            WarehouseTransaction transaction = getValidTransaction(transactionId);
            WarehouseDTO lotDTO = objectMapper.readValue(transaction.getWarehouseData(), WarehouseDTO.class);
            Warehouse entity = convertToEntity(lotDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            Warehouse updated = lotRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getWarehouseId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_SUPPLIER", "Warehouse updated successfully", ip, userAgent, loginId);
            return ApiResponse.success(updated.getWarehouseId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_SUPPLIER_FAIL", "Failed to confirm lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateWarehouse(String transactionId, String ip, String userAgent, String loginId) {
        try {
            WarehouseTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_SUPPLIER", "Warehouse update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Warehouse update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_SUPPLIER_FAIL", "Failed to cancel lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteWarehouse(String transactionId, Long lotId, String ip, String userAgent, String loginId) {
        try {
            Warehouse lot = lotRepository.findById(lotId)
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            String lotData = objectMapper.writeValueAsString(convertToDTO(lot));
            createTransaction(transactionId, "DELETE", lotData, null);
            sendLog("TRY_DELETE_SUPPLIER", "Warehouse deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_SUPPLIER_FAIL", "Failed to prepare lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteWarehouse(String transactionId, String ip, String userAgent, String loginId) {
        try {
            WarehouseTransaction transaction = getValidTransaction(transactionId);
            WarehouseDTO lotDTO = objectMapper.readValue(transaction.getWarehouseData(), WarehouseDTO.class);
            lotRepository.deleteById(lotDTO.getWarehouseId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_SUPPLIER", "Warehouse deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Warehouse deleted successfully");
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_SUPPLIER_FAIL", "Failed to confirm lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteWarehouse(String transactionId, String ip, String userAgent, String loginId) {
        try {
            WarehouseTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_SUPPLIER", "Warehouse deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Warehouse deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_SUPPLIER_FAIL", "Failed to cancel lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot deletion: " + e.getMessage(), e);
        }
    }
}
