package kr.cms.inventoryBff.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryBff.client.InventoryClient;
import kr.cms.inventoryBff.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryAggregationService {

    private final InventoryClient inventoryClient;

    public ApiResponse<List<InventoryDTO>> getAggregatedInventories(String ip, String userAgent, String loginId) {
        return inventoryClient.getInventoryList(ip, userAgent, loginId).block();
    }
}
