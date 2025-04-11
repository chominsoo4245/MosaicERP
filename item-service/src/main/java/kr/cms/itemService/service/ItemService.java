package kr.cms.itemService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    ApiResponse<ItemDTO> createItem(ItemDTO itemDTO);
    ApiResponse<ItemDTO> updateItem(Long itemId, ItemDTO itemDTO);
    ApiResponse<ItemDTO> getItemById(Long itemId);
    ApiResponse<List<ItemDTO>> getAllItems();
    ApiResponse<String> deleteItem(Long itemId);
}
