package kr.cms.itemService.service;

import jakarta.persistence.criteria.Predicate;
import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.dto.SearchDataDTO;
import kr.cms.itemService.entity.Item;
import kr.cms.itemService.logging.LogSender;
import kr.cms.itemService.repository.ItemRepository;
import kr.cms.itemService.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kr.cms.itemService.util.DataUtil.convertToEntity;
import static kr.cms.itemService.util.DataUtil.convertToDTO;

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
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ItemDTO>> getAllItems(String ip, String userAgent, String loginId) {
        try {
            List<Item> itemList = itemRepository.findAll();
            List<ItemDTO> dtoList = itemList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_ITEMS_SUCCESS", "All items retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_ITEMS_FAIL", "Failed to retrieve items: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ItemDTO>> getSearchItems(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId){
        if (isAllFieldsNull(searchDataDTO)) {
            return getAllItems(ip, userAgent, loginId);
        }

        try {
            Specification<Item> spec = createSpecification(searchDataDTO);
            List<Item> itemList = itemRepository.findAll(spec);
            List<ItemDTO> dtoList = itemList.stream()
                    .map(DataUtil::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("SEARCH_ITEMS_SUCCESS", "Items searched successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_ITEMS_FAIL", "Failed to search items: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Transactional
    public ApiResponse<Long> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {

        try {
            Item entity = convertToEntity(itemDTO);
            LocalDateTime now = LocalDateTime.now();
            entity.setItemId(null);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Item saved = itemRepository.save(entity);
            logSender.sendLog("CREATE_ITEM_SUCCESS", "Item created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getItemId());
        } catch (Exception e) {
            logSender.sendLog("CREATE_ITEM_FAIL", "Failed to create item: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

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
            throw e;
        }
    }

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
            throw e;
        }
    }

    private boolean isAllFieldsNull(SearchDataDTO searchDataDTO) {
        return (searchDataDTO.getKeyword() == null || searchDataDTO.getKeyword().trim().isEmpty())
                && searchDataDTO.getCreateStart() == null
                && searchDataDTO.getCreateEnd() == null
                && searchDataDTO.getUpdateStart() == null
                && searchDataDTO.getUpdateEnd() == null;
    }

    private Specification<Item> createSpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String pattern = "%" + searchDataDTO.getKeyword().toLowerCase() + "%";
                Predicate keywordPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern)
                );
                predicate = cb.and(predicate, keywordPredicate);
            }

            if (searchDataDTO.getCreateStart() != null) {
                LocalDateTime createEnd = (searchDataDTO.getCreateEnd() != null) ? searchDataDTO.getCreateEnd() : LocalDateTime.now();
                predicate = cb.and(predicate, cb.between(root.get("createAt"), searchDataDTO.getCreateStart(), createEnd));
            }

            if (searchDataDTO.getUpdateStart() != null) {
                LocalDateTime updateEnd = (searchDataDTO.getUpdateEnd() != null) ? searchDataDTO.getUpdateEnd() : LocalDateTime.now();
                predicate = cb.and(predicate, cb.between(root.get("updateAt"), searchDataDTO.getUpdateStart(), updateEnd));
            }

            if (searchDataDTO.getCategoryId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category_id"), searchDataDTO.getCategoryId()));
            }

            if (searchDataDTO.getSupplierId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("default_supplier_id"), searchDataDTO.getSupplierId()));
            }

            return predicate;
        };
    }
}
