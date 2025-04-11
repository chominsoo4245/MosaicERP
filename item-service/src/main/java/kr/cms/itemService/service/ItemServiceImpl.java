package kr.cms.itemService.service;

import jakarta.transaction.Transactional;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.AuditLogDTO;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.entity.Item;
import kr.cms.itemService.logging.LogSender;
import kr.cms.itemService.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final LogSender logSender;

    @Override
    @Transactional
    public ApiResponse<String> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        try {
            Item item = convertToEntity(itemDTO);
            itemRepository.save(item);
            logSender.sendAuditLog(new AuditLogDTO(
                    "CREATE_ITEM_SUCCESS",
                    loginId,
                    "Item created successfully",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.successWithMessage("Item created successfully");
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDTO(
                    "CREATE_ITEM_FAIL",
                    loginId,
                    "Failed to create item: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail("Failed to create item: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<String> updateItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        try {
            itemRepository.findById(itemDTO.getItemId()).orElseThrow(() -> new RuntimeException("Item not found"));

            Item resultItem = convertToEntity(itemDTO);
            itemRepository.save(resultItem);

            logSender.sendAuditLog(new AuditLogDTO(
                    "UPDATE_ITEM_SUCCESS",
                    loginId,
                    "Item updated successfully",
                    ip, // IP
                    userAgent, // User-Agent
                    LocalDateTime.now()
            ));
            return ApiResponse.successWithMessage("UPDATE_ITEM_SUCCESS");
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDTO(
                    "UPDATE_ITEM_FAIL",
                    loginId,
                    "Failed to update item: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail("Failed to update item: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<ItemDTO> getItemById(Long itemId, String ip, String userAgent, String loginId) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            logSender.sendAuditLog(new AuditLogDTO(
                    "GET_ITEM_SUCCESS",
                    loginId,
                    "Item retrieved successfully",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(convertToDTO(item));
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDTO(
                    "GET_ITEM_FAIL",
                    loginId,
                    "Failed to retrieve item: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail("Failed to retrieve item: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<ItemDTO>> getAllItems(String ip, String userAgent, String loginId) {
        try {
            List<Item> itemList = itemRepository.findAll();
            List<ItemDTO> dtoList = itemList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendAuditLog(new AuditLogDTO(
                    "GET_ALL_ITEMS_SUCCESS",
                    loginId,
                    "All items retrieved successfully",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDTO(
                    "GET_ALL_ITEMS_FAIL",
                    loginId,
                    "Failed to retrieve items: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail("Failed to retrieve items: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<String> deleteItem(Long itemId, String ip, String userAgent, String loginId) {
        try {
            if (!itemRepository.existsById(itemId)) {
                logSender.sendAuditLog(new AuditLogDTO(
                        "DELETE_ITEM_FAIL",
                        loginId,
                        "Item not found for deletion",
                        ip,
                        userAgent,
                        LocalDateTime.now()
                ));
                return ApiResponse.fail("Item not found");
            }
            itemRepository.deleteById(itemId);

            logSender.sendAuditLog(new AuditLogDTO(
                    "DELETE_ITEM_SUCCESS",
                    loginId,
                    "Item deleted successfully",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.successWithMessage("Item deleted successfully");
        } catch (Exception e) {
            logSender.sendAuditLog(new AuditLogDTO(
                    "DELETE_ITEM_FAIL",
                    loginId,
                    "Failed to delete item: " + e.getMessage(),
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            return ApiResponse.fail("Failed to delete item: " + e.getMessage());
        }
    }

    private Item convertToEntity(ItemDTO dto) {
        Item item = new Item();
        item.setCategoryId(dto.getCategoryId());
        item.setCode(dto.getCode());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setUnit(dto.getUnit());
        item.setCost(dto.getCost());
        item.setPrice(dto.getPrice());
        item.setIsLotTracked(dto.getIsLotTracked() != null ? dto.getIsLotTracked() : false);
        item.setDefaultSupplierId(dto.getDefaultSupplierId());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return item;
    }

    private ItemDTO convertToDTO(Item item) {
        return new ItemDTO(
                item.getItemId(),
                item.getCategoryId(),
                item.getCode(),
                item.getName(),
                item.getDescription(),
                item.getUnit(),
                item.getCost(),
                item.getPrice(),
                item.getIsLotTracked(),
                item.getDefaultSupplierId(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
