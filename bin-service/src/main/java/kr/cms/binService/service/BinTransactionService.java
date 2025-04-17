package kr.cms.binService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.binService.util.DataUtil;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.binService.dto.BinDTO;
import kr.cms.binService.entity.Bin;
import kr.cms.binService.entity.BinTransaction;
import kr.cms.binService.logging.LogSender;
import kr.cms.binService.repository.BinRepository;
import kr.cms.binService.repository.BinTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BinTransactionService {
    private final BinTransactionRepository transactionRepository;
    private final BinRepository binRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private BinTransaction getValidTransaction(String transactionId) {
        BinTransaction transaction = transactionRepository.findByTransactionId(transactionId)
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

    private void createTransaction(String transactionId,String operation, String binData, String originalData) {
        BinTransaction transaction = new BinTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setBinData(binData);
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

    private void validateBinData(BinDTO binDTO){

    }

    @Transactional
    public ApiResponse<String> tryCreateBin(String transactionId, BinDTO binDTO, String ip, String userAgent, String loginId){
        try{
            validateBinData(binDTO);
            String binData = objectMapper.writeValueAsString(binDTO);
            createTransaction(transactionId, "CREATE", binData, null);
            sendLog("TRY_CREATE_SUPPLIER", "bin creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        }catch (Exception e){
            sendLog("TRY_CREATE_SUPPLIER_FAIL", "Failed to prepare bin creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare bin creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateBin(String transactionId, String ip, String userAgent, String loginId){
        try{
            BinTransaction transaction = getValidTransaction(transactionId);

            BinDTO binDTO = objectMapper.readValue(transaction.getBinData(), BinDTO.class);
            Bin entity = DataUtil.convertToEntity(binDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setBinId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Bin saved = binRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getBinId().toString());
            transactionRepository.save(transaction);
            sendLog("CONFIRM_CREATE_SUPPLIER", "Bin created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getBinId().toString());
        }catch (Exception e){
            sendLog("CONFIRM_CREATE_SUPPLIER_FAIL", "Failed to confirm bin creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm bin creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateBin(String transactionId, String ip, String userAgent, String loginId) {
        try {
            BinTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_SUPPLIER", "Bin creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Bin creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_SUPPLIER_FAIL", "Failed to cancel bin creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel bin creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateBin(String transactionId, BinDTO binDTO, String ip, String userAgent, String loginId) {
        try {
            Bin existingBin = binRepository.findById(binDTO.getBinId())
                    .orElseThrow(() -> new RuntimeException("Bin not found"));
            validateBinData(binDTO);

            BinDTO originalBinDTO = DataUtil.convertToDTO(existingBin);
            String binData = objectMapper.writeValueAsString(binDTO);
            String originalData = objectMapper.writeValueAsString(originalBinDTO);
            createTransaction(transactionId, "UPDATE", binData, originalData);

            sendLog("TRY_UPDATE_SUPPLIER", "Bin update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_SUPPLIER_FAIL", "Failed to prepare bin update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare bin update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateBin(String transactionId, String ip, String userAgent, String loginId) {
        try {
            BinTransaction transaction = getValidTransaction(transactionId);
            BinDTO binDTO = objectMapper.readValue(transaction.getBinData(), BinDTO.class);
            Bin entity = DataUtil.convertToEntity(binDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            Bin updated = binRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getBinId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_SUPPLIER", "Bin updated successfully", ip, userAgent, loginId);
            return ApiResponse.success(updated.getBinId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_SUPPLIER_FAIL", "Failed to confirm bin update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm bin update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateBin(String transactionId, String ip, String userAgent, String loginId) {
        try {
            BinTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_SUPPLIER", "Bin update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Bin update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_SUPPLIER_FAIL", "Failed to cancel bin update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel bin update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteBin(String transactionId, Long binId, String ip, String userAgent, String loginId) {
        try {
            Bin bin = binRepository.findById(binId)
                    .orElseThrow(() -> new RuntimeException("Bin not found"));
            String binData = objectMapper.writeValueAsString(DataUtil.convertToDTO(bin));
            createTransaction(transactionId, "DELETE", binData, null);
            sendLog("TRY_DELETE_SUPPLIER", "Bin deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_SUPPLIER_FAIL", "Failed to prepare bin deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare bin deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteBin(String transactionId, String ip, String userAgent, String loginId) {
        try {
            BinTransaction transaction = getValidTransaction(transactionId);
            BinDTO binDTO = objectMapper.readValue(transaction.getBinData(), BinDTO.class);
            binRepository.deleteById(binDTO.getBinId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_SUPPLIER", "Bin deleted successfully", ip, userAgent, loginId);
            return ApiResponse.success(binDTO.getBinId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_SUPPLIER_FAIL", "Failed to confirm bin deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm bin deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteBin(String transactionId, String ip, String userAgent, String loginId) {
        try {
            BinTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_SUPPLIER", "Bin deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Bin deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_SUPPLIER_FAIL", "Failed to cancel bin deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel bin deletion: " + e.getMessage(), e);
        }
    }
}
