package kr.cms.lotService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.lotService.dto.LotDTO;
import kr.cms.lotService.entity.Lot;
import kr.cms.lotService.entity.LotTransaction;
import kr.cms.lotService.logging.LogSender;
import kr.cms.lotService.repository.LotRepository;
import kr.cms.lotService.repository.LotTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.cms.lotService.util.DataUtil.convertToDTO;
import static kr.cms.lotService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class LotTransactionService {
    private final LotTransactionRepository transactionRepository;
    private final LotRepository lotRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private LotTransaction getValidTransaction(String transactionId) {
        LotTransaction transaction = transactionRepository.findByTransactionId(transactionId)
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
        LotTransaction transaction = new LotTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setLotData(lotData);
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

    private void validateLotData(LotDTO lotDTO){

    }

    @Transactional
    public ApiResponse<String> tryCreateLot(String transactionId, LotDTO lotDTO, String ip, String userAgent, String loginId){
        try{
            validateLotData(lotDTO);
            String lotData = objectMapper.writeValueAsString(lotDTO);
            createTransaction(transactionId,"CREATE", lotData, null);
            sendLog("TRY_CREATE_SUPPLIER", "lot creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        }catch (Exception e){
            sendLog("TRY_CREATE_SUPPLIER_FAIL", "Failed to prepare lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateLot(String transactionId, LotDTO resLotDTO, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);

            LotDTO lotDTO = objectMapper.readValue(transaction.getLotData(), LotDTO.class);
            lotDTO.setItemId(resLotDTO.getItemId());
            lotDTO.setLotNumber(resLotDTO.getLotNumber());

            transaction.setLotData(objectMapper.writeValueAsString(lotDTO));
            transactionRepository.save(transaction);

            // 엔티티 변환 및 저장
            Lot entity = convertToEntity(lotDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setLotId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Lot saved = lotRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getLotId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_CREATE_LOT", "Lot created successfully with ID: " + saved.getLotId() + " for item: " + resLotDTO.getItemId(),
                    ip, userAgent, loginId);
            return ApiResponse.success(saved.getLotId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_CREATE_LOT_FAIL", "Failed to confirm lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateLot(String transactionId, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_SUPPLIER", "Lot creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Lot creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_SUPPLIER_FAIL", "Failed to cancel lot creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateLot(String transactionId, LotDTO lotDTO, String ip, String userAgent, String loginId) {
        try {
            Lot existingLot = lotRepository.findById(lotDTO.getLotId())
                    .orElseThrow(() -> new RuntimeException("Lot not found"));
            validateLotData(lotDTO);

            LotDTO originalLotDTO = convertToDTO(existingLot);
            String lotData = objectMapper.writeValueAsString(lotDTO);
            String originalData = objectMapper.writeValueAsString(originalLotDTO);
            createTransaction(transactionId,"UPDATE", lotData, originalData);

            sendLog("TRY_UPDATE_SUPPLIER", "Lot update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_SUPPLIER_FAIL", "Failed to prepare lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateLot(String transactionId, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);
            LotDTO lotDTO = objectMapper.readValue(transaction.getLotData(), LotDTO.class);
            Lot entity = convertToEntity(lotDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            Lot updated = lotRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getLotId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_SUPPLIER", "Lot updated successfully", ip, userAgent, loginId);
            return ApiResponse.success(updated.getLotId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_SUPPLIER_FAIL", "Failed to confirm lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateLot(String transactionId, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_SUPPLIER", "Lot update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Lot update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_SUPPLIER_FAIL", "Failed to cancel lot update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteLot(String transactionId, Long lotId, String ip, String userAgent, String loginId) {
        try {
            Lot lot = lotRepository.findById(lotId)
                    .orElseThrow(() -> new RuntimeException("Lot not found"));
            String lotData = objectMapper.writeValueAsString(convertToDTO(lot));
            createTransaction(transactionId, "DELETE", lotData, null);
            sendLog("TRY_DELETE_SUPPLIER", "Lot deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_SUPPLIER_FAIL", "Failed to prepare lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare lot deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteLot(String transactionId, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);
            LotDTO lotDTO = objectMapper.readValue(transaction.getLotData(), LotDTO.class);
            lotRepository.deleteById(lotDTO.getLotId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_SUPPLIER", "Lot deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Lot deleted successfully");
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_SUPPLIER_FAIL", "Failed to confirm lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm lot deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteLot(String transactionId, String ip, String userAgent, String loginId) {
        try {
            LotTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_SUPPLIER", "Lot deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Lot deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_SUPPLIER_FAIL", "Failed to cancel lot deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel lot deletion: " + e.getMessage(), e);
        }
    }
}
