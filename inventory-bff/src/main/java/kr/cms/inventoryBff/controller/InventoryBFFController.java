package kr.cms.inventoryBff.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryBff.dto.InventoryDTO;
import kr.cms.inventoryBff.service.InventoryAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory-bff")
@RequiredArgsConstructor
public class InventoryBFFController {
    private final InventoryAggregationService inventoryAggregationService;

    @GetMapping("/aggregated")
    public ApiResponse<List<InventoryDTO>> getAggregatedInventories(
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ){
        return inventoryAggregationService.getAggregatedInventories(ip, userAgent, loginId);
    }
}
