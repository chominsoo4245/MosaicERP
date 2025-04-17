package kr.cms.serialService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.enums.TransactionStatus;
import kr.cms.serialService.dto.SerialDTO;
import kr.cms.serialService.entity.Serial;
import kr.cms.serialService.entity.SerialTransaction;
import kr.cms.serialService.logging.LogSender;
import kr.cms.serialService.repository.SerialRepository;
import kr.cms.serialService.repository.SerialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kr.cms.serialService.util.DataUtil.convertToDTO;
import static kr.cms.serialService.util.DataUtil.convertToEntity;

@Service
@RequiredArgsConstructor
public class SerialTransactionService {
    private final SerialTransactionRepository transactionRepository;
    private final SerialRepository serialRepository;
    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    private SerialTransaction getValidTransaction(String transactionId) {
        SerialTransaction transaction = transactionRepository.findByTransactionId(transactionId)
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

    private void createTransaction(String transactionId, String operation, String serialData, String originalData) {
        SerialTransaction transaction = new SerialTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setSerialData(serialData);
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

    private void validateSerialData(SerialDTO serialDTO){

    }

    @Transactional
    public ApiResponse<String> tryCreateSerial(String transactionId, SerialDTO serialDTO, String ip, String userAgent, String loginId){
        try{
            validateSerialData(serialDTO);
            String serialData = objectMapper.writeValueAsString(serialDTO);
            createTransaction(transactionId,"CREATE", serialData, null);
            sendLog("TRY_CREATE_SUPPLIER", "serial creation prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        }catch (Exception e){
            sendLog("TRY_CREATE_SUPPLIER_FAIL", "Failed to prepare serial creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare serial creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmCreateSerial(String transactionId, SerialDTO resSerialDTO, String ip, String userAgent, String loginId){
        try{
            SerialTransaction transaction = getValidTransaction(transactionId);

            SerialDTO serialDTO = objectMapper.readValue(transaction.getSerialData(), SerialDTO.class);
            serialDTO.setItemId(resSerialDTO.getItemId());
            serialDTO.setLotId(resSerialDTO.getLotId());
            serialDTO.setSerialNumber(resSerialDTO.getSerialNumber());
            transaction.setSerialData(objectMapper.writeValueAsString(serialDTO));
            transactionRepository.save(transaction);

            Serial entity = convertToEntity(serialDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setSerialId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Serial saved = serialRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(now);
            transaction.setResultId(saved.getSerialId().toString());
            transactionRepository.save(transaction);
            sendLog("CONFIRM_CREATE_SUPPLIER", "Serial created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getSerialId().toString());
        }catch (Exception e){
            sendLog("CONFIRM_CREATE_SUPPLIER_FAIL", "Failed to confirm serial creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm serial creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelCreateSerial(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SerialTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_CREATE_SUPPLIER", "Serial creation cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Serial creation cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_CREATE_SUPPLIER_FAIL", "Failed to cancel serial creation: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel serial creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryUpdateSerial(String transactionId, SerialDTO serialDTO, String ip, String userAgent, String loginId) {
        try {
            Serial existingSerial = serialRepository.findById(serialDTO.getSerialId())
                    .orElseThrow(() -> new RuntimeException("Serial not found"));
            validateSerialData(serialDTO);

            SerialDTO originalSerialDTO = convertToDTO(existingSerial);
            String serialData = objectMapper.writeValueAsString(serialDTO);
            String originalData = objectMapper.writeValueAsString(originalSerialDTO);
            createTransaction(transactionId,"UPDATE", serialData, originalData);

            sendLog("TRY_UPDATE_SUPPLIER", "Serial update prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_UPDATE_SUPPLIER_FAIL", "Failed to prepare serial update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare serial update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmUpdateSerial(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SerialTransaction transaction = getValidTransaction(transactionId);
            SerialDTO serialDTO = objectMapper.readValue(transaction.getSerialData(), SerialDTO.class);
            Serial entity = convertToEntity(serialDTO);
            entity.setUpdatedAt(LocalDateTime.now());
            Serial updated = serialRepository.save(entity);

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction.setResultId(entity.getSerialId().toString());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_UPDATE_SUPPLIER", "Serial updated successfully", ip, userAgent, loginId);
            return ApiResponse.success(updated.getSerialId().toString());
        } catch (Exception e) {
            sendLog("CONFIRM_UPDATE_SUPPLIER_FAIL", "Failed to confirm serial update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm serial update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelUpdateSerial(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SerialTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CANCEL_UPDATE_SUPPLIER", "Serial update cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Serial update cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_UPDATE_SUPPLIER_FAIL", "Failed to cancel serial update: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel serial update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> tryDeleteSerial(String transactionId,Long serialId, String ip, String userAgent, String loginId) {
        try {
            Serial serial = serialRepository.findById(serialId)
                    .orElseThrow(() -> new RuntimeException("Serial not found"));
            String serialData = objectMapper.writeValueAsString(convertToDTO(serial));
            createTransaction(transactionId, "DELETE", serialData, null);
            sendLog("TRY_DELETE_SUPPLIER", "Serial deletion prepared", ip, userAgent, loginId);
            return ApiResponse.success(transactionId);
        } catch (Exception e) {
            sendLog("TRY_DELETE_SUPPLIER_FAIL", "Failed to prepare serial deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to prepare serial deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> confirmDeleteSerial(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SerialTransaction transaction = getValidTransaction(transactionId);
            SerialDTO serialDTO = objectMapper.readValue(transaction.getSerialData(), SerialDTO.class);
            serialRepository.deleteById(serialDTO.getSerialId());

            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            sendLog("CONFIRM_DELETE_SUPPLIER", "Serial deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Serial deleted successfully");
        } catch (Exception e) {
            sendLog("CONFIRM_DELETE_SUPPLIER_FAIL", "Failed to confirm serial deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to confirm serial deletion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ApiResponse<String> cancelDeleteSerial(String transactionId, String ip, String userAgent, String loginId) {
        try {
            SerialTransaction transaction = getValidTransaction(transactionId);
            transaction.setStatus(TransactionStatus.CANCELLED);
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            sendLog("CANCEL_DELETE_SUPPLIER", "Serial deletion cancelled", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Serial deletion cancelled");
        } catch (Exception e) {
            sendLog("CANCEL_DELETE_SUPPLIER_FAIL", "Failed to cancel serial deletion: " + e.getMessage(), ip, userAgent, loginId);
            throw new RuntimeException("Failed to cancel serial deletion: " + e.getMessage(), e);
        }
    }
}
