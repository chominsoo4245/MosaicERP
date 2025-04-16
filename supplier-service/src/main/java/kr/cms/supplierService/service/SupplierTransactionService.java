package kr.cms.supplierService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.supplierService.dto.SupplierDTO;
import kr.cms.supplierService.entity.Supplier;
import kr.cms.supplierService.entity.SupplierTransaction;
import kr.cms.supplierService.logging.LogSender;
import kr.cms.supplierService.repository.SupplierRepository;
import kr.cms.supplierService.repository.SupplierTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.cms.supplierService.util.DataUtil.convertToDTO;
import static kr.cms.supplierService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class SupplierTransactionService {
    private final SupplierTransactionRepository transactionRepository; 
    private final LogSender logSender;
    private final ObjectMapper objectMapper;
    private final SupplierRepository supplierRepository;

    private SupplierTransaction getValidTransaction(String transactionId) {
        SupplierTransaction transaction = transactionRepository.findByTransactionId(transactionId)
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
    
    private String createTransaction(String operation, String supplierData, String originalData) {
        String transactionId = UUID.randomUUID().toString();
        SupplierTransaction transaction = new SupplierTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setSupplierData(supplierData);
        transaction.setOriginalData(originalData);
        transaction.setStatus(TransactionStatus.TRYING);
        transaction.setOperation(operation);
        LocalDateTime now = LocalDateTime.now();
        transaction.setCreatedAt(now);
        transaction.setExpireAt(now.plusMinutes(10));
        transactionRepository.save(transaction);
        return transactionId;
    }
    
    private void sendLog(String action, String description, String ip, String userAgent, String loginId) {
        logSender.sendLog(action, description, ip, userAgent, loginId);
    }
    
    private void validateSupplierData(SupplierDTO supplierDTO){
        
    }
    
    @Transactional
    public ApiResponse<String> tryCreateSupplier(SupplierDTO supplierDTO, String ip, String userAgent, String loginId){
        try{
            validateSupplierData(supplierDTO);
            String supplierData = objectMapper.writeValueAsString(supplierDTO);
            String transactionId = createTransaction("CREATE", supplierData, null);
            sendLog("TRY_CREATE_SUPPLIER", "supplier creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        }catch (Exception e){
            sendLog("TRY_CREATE_SUPPLIER_FAIL", "Failed to prepare supplier creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare supplier creation: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public ApiResponse<String> confirmCreateSupplier(String transactionId, String ip, String userAgent, String loginId){
        try{
            SupplierTransaction transaction = getValidTransaction(transactionId);
            
            SupplierDTO supplierDTO = objectMapper.readValue(transaction.getSupplierData(), SupplierDTO.class);
            Supplier entity = convertToEntity(supplierDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Supplier saved = supplierRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getId().toString());
            transactionRepository.save(transaction);
            sendLog("CONFIRM_CREATE_SUPPLIER", "Supplier created successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier created with ID: " + saved.getId());
        }catch (Exception e){
            sendLog("CONFIRM_CREATE_SUPPLIER_FAIL", "Failed to confirm supplier creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm supplier creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateSupplier(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SupplierTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_SUPPLIER", "Supplier creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_SUPPLIER_FAIL", "Failed to cancel supplier creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel supplier creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateSupplier(SupplierDTO supplierDTO, String ip, String userAgent, String loginId) {
        try {
            Supplier existingSupplier = supplierRepository.findById(supplierDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            validateSupplierData(supplierDTO);

            SupplierDTO originalSupplierDTO = convertToDTO(existingSupplier);
            String supplierData = objectMapper.writeValueAsString(supplierDTO);
            String originalData = objectMapper.writeValueAsString(originalSupplierDTO);
            String transactionId = createTransaction("UPDATE", supplierData, originalData);

            sendLog("TRY_UPDATE_SUPPLIER", "Supplier update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_SUPPLIER_FAIL", "Failed to prepare supplier update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare supplier update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateSupplier(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SupplierTransaction transaction = getValidTransaction(transactionId);
            SupplierDTO supplierDTO = objectMapper.readValue(transaction.getSupplierData(), SupplierDTO.class);
            Supplier entity = convertToEntity(supplierDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            supplierRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_SUPPLIER", "Supplier updated successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier updated successfully");
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_SUPPLIER_FAIL", "Failed to confirm supplier update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm supplier update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateSupplier(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SupplierTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_SUPPLIER", "Supplier update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_SUPPLIER_FAIL", "Failed to cancel supplier update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel supplier update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteSupplier(Long supplierId, String ip, String userAgent, String loginId) {
        try {
            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            String supplierData = objectMapper.writeValueAsString(convertToDTO(supplier));
            String transactionId = createTransaction("DELETE", supplierData, null);
            sendLog("TRY_DELETE_SUPPLIER", "Supplier deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_SUPPLIER_FAIL", "Failed to prepare supplier deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare supplier deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteSupplier(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SupplierTransaction transaction = getValidTransaction(transactionId);
            SupplierDTO supplierDTO = objectMapper.readValue(transaction.getSupplierData(), SupplierDTO.class);
            supplierRepository.deleteById(supplierDTO.getId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_SUPPLIER", "Supplier deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier deleted successfully");
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_SUPPLIER_FAIL", "Failed to confirm supplier deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm supplier deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteSupplier(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SupplierTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_SUPPLIER", "Supplier deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Supplier deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_SUPPLIER_FAIL", "Failed to cancel supplier deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel supplier deletion: " + e.getMessage(), e);
        }
    }
}
