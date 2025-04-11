package kr.cms.itemService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.provider.HeaderProvider;
import kr.cms.itemService.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item-service")
@RequiredArgsConstructor
public class ItemController {
    private final HeaderProvider headerProvider;

    @GetMapping
    public ApiResponse<ItemDTO> getItem() {
        return null;
    }
}
