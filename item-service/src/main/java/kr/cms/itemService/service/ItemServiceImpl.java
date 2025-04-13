package kr.cms.itemService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.entity.Item;
import kr.cms.itemService.logging.LogSender;
import kr.cms.itemService.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final LogSender logSender;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ItemDTO> getItemById(Long itemId, String ip, String userAgent, String loginId) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            logSender.sendLog("GET_ITEM_SUCCESS", "Item retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(item));
        } catch (Exception e) {logSender.sendLog("GET_ITEM_FAIL", "Failed to retrieve item: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieve item: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ItemDTO>> getAllItems(String ip, String userAgent, String loginId) {
        try {
            List<Item> itemList = itemRepository.findAll();
            List<ItemDTO> dtoList = itemList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_ITEMS_SUCCESS", "All items retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {logSender.sendLog("GET_ALL_ITEMS_FAIL", "Failed to retrieve items: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieve items: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        try {
            Item entity = convertToEntity(itemDTO);
            itemRepository.save(entity);
            logSender.sendLog("CREATE_ITEM_SUCCESS", "Item created successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item created successfully");
        } catch (Exception e) {
            logSender.sendLog("CREATE_ITEM_FAIL", "Failed to create item: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to create item: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> updateItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        try {
            itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            Item entity = convertToEntity(itemDTO);
            itemRepository.save(entity);
            logSender.sendLog("UPDATE_ITEM_SUCCESS", "Item updated successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item updated successfully");
        } catch (Exception e) {
            logSender.sendLog("UPDATE_ITEM_FAIL", "Failed to update item: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to update item: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteItem(Long itemId, String ip, String userAgent, String loginId) {
        try {
            if (!itemRepository.existsById(itemId)) {logSender.sendLog("DELETE_ITEM_FAIL", "Item not found for deletion", ip, userAgent, loginId);
                return ApiResponse.fail("Item not found");
            }
            itemRepository.deleteById(itemId);
            logSender.sendLog("DELETE_ITEM_SUCCESS", "Item deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Item deleted successfully");
        } catch (Exception e) {logSender.sendLog("DELETE_ITEM_FAIL", "Failed to delete item: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to delete item: " + e.getMessage());
        }
    }

    private Item convertToEntity(ItemDTO dto) {
        Item entity = new Item();
        entity.setCategoryId(dto.getCategoryId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setCost(dto.getCost());
        entity.setPrice(dto.getPrice());
        entity.setIsLotTracked(dto.getIsLotTracked() != null ? dto.getIsLotTracked() : false);
        entity.setDefaultSupplierId(dto.getDefaultSupplierId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    private ItemDTO convertToDTO(Item entity) {
        ItemDTO dto = new ItemDTO();
        dto.setCategoryId(entity.getCategoryId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUnit(entity.getUnit());
        dto.setCost(entity.getCost());
        dto.setPrice(entity.getPrice());
        dto.setIsLotTracked(entity.getIsLotTracked() != null ? entity.getIsLotTracked() : false);
        dto.setDefaultSupplierId(entity.getDefaultSupplierId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
