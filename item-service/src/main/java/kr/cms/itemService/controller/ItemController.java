package kr.cms.itemService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-service")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<ItemDTO> getItem(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemService.getItemById(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/list")
    public ApiResponse<List<ItemDTO>> getItemList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemService.getAllItems(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PostMapping("/create")
    public ApiResponse<String> addItem(@RequestBody ItemDTO itemDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemService.createItem(itemDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PutMapping("/update")
    public ApiResponse<String> editItem(@RequestBody ItemDTO itemDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemService.updateItem(itemDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeItem(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemService.deleteItem(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }
}
