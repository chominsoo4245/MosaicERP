package kr.cms.itemBff.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemBff.dto.*;
import kr.cms.itemBff.service.ItemBFFService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/item-bff")
@RequiredArgsConstructor
public class ItemBFFController {

    private final ItemBFFService itemBffService;
    private final HeaderProvider headerProvider;
    private final ItemBFFService itemBFFService;

    @GetMapping("/formDataInit")
    public Mono<ApiResponse<FormDataInitDTO>> getFormDataInit() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemBffService.getFormDataInit(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getIp());
    }

    @GetMapping("/getItemList")
    public Mono<ApiResponse<List<ItemResponseDTO>>> getItemList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemBffService.getItemList(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getIp());
    }

    @PostMapping("/create")
    public Mono<ApiResponse<CreateItemResponse>> createItem(@RequestBody CreateItemRequest request) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemBffService.createItem(request, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getIp());
    }

    @GetMapping("/{itemId}")
    public Mono<ApiResponse<ItemDetailDTO>> getItemDetail(@PathVariable Long itemId) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemBFFService.getItemDetail(itemId, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getIp());
    }
}
