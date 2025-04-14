package kr.cms.itemBff.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemBff.dto.AggregatedItemDTO;
import kr.cms.itemBff.dto.ItemDTO;
import kr.cms.itemBff.dto.ItemFormInitDTO;
import kr.cms.itemBff.service.ItemAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-bff")
@RequiredArgsConstructor
public class ItemBFFController {

    private final ItemAggregationService itemAggregationService;
    private final HeaderProvider headerProvider;

    @GetMapping("/aggregated")
    public ApiResponse<List<AggregatedItemDTO>> getAggregatedItems(){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemAggregationService.getAggregatedItems(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/form-init")
    public ApiResponse<ItemFormInitDTO> getFormInitData() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemAggregationService.getFormInitData(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PostMapping("/add")
    public ApiResponse<String> addItem(@RequestBody ItemDTO dto){
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return itemAggregationService.createItem(dto, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

}
