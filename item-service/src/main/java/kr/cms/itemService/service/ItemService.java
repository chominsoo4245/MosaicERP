package kr.cms.itemService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    ApiResponse<ItemDTO> getItemById(Long itemId, String ip, String userAgent, String loginId);

    ApiResponse<List<ItemDTO>> getAllItems(String ip, String userAgent, String loginId);

    ApiResponse<String> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> updateItem(ItemDTO itemDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> deleteItem(Long itemId, String ip, String userAgent, String loginId);
}
