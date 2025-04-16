package kr.cms.itemService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemService.dto.ItemDTO;
import kr.cms.itemService.dto.SearchDataDTO;
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
        try {
            return itemService.getItemById(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieve item: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<ItemDTO>> getItemList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return itemService.getAllItems(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieve items: " + e.getMessage());
        }

    }

    @PostMapping("/search")
    public ApiResponse<List<ItemDTO>> getItemSearchList(@RequestBody SearchDataDTO dto) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return itemService.getSearchItems(dto, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e) {
            return ApiResponse.fail("Failed to search items: " + e.getMessage());
        }
    }
}
