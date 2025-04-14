package kr.cms.itemService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.dto.SearchDataDTO;

import java.util.List;

public interface ItemService {
    ApiResponse<ItemDTO> getItemById(Long itemId, String ip, String userAgent, String loginId);

    ApiResponse<List<ItemDTO>> getAllItems(String ip, String userAgent, String loginId);

    ApiResponse<List<ItemDTO>> getSearchItems(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId);

    ApiResponse<Long> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> updateItem(ItemDTO itemDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> deleteItem(Long itemId, String ip, String userAgent, String loginId);
}
