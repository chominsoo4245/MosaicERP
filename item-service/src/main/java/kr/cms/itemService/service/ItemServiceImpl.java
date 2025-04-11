package kr.cms.itemService.service;

import jakarta.transaction.Transactional;
import kr.cms.common.dto.ApiResponse;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.entity.Item;
import kr.cms.itemService.logging.LogSender;
import kr.cms.itemService.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final LogSender logSender;

    @Override
    @Transactional
    public ApiResponse<ItemDTO> createItem(ItemDTO itemDTO) {
        try{
            Item item = convertToEntity();
            item = itemRepository.save(item);
            ItemDTO savedDTO = convertToDTO();
            return ApiResponse.success();
        } catch (Exception e){
            return ApiResponse.fail("Failed to create item :" + e.getMessage());
        }
    }

    @Override
    public ApiResponse<ItemDTO> updateItem(Long itemId, ItemDTO itemDTO) {
        return null;
    }

    @Override
    public ApiResponse<ItemDTO> getItemById(Long itemId) {
        return null;
    }

    @Override
    public ApiResponse<List<ItemDTO>> getAllItems() {
        return null;
    }

    @Override
    public ApiResponse<String> deleteItem(Long itemId) {
        return null;
    }
}
