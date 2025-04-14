package kr.cms.itemBff.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.client.CategoryClient;
import kr.cms.itemBff.client.ItemClient;
import kr.cms.itemBff.client.SupplierClient;
import kr.cms.itemBff.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemAggregationService {
    private final ItemClient itemClient;
    private final CategoryClient categoryClient;
    private final SupplierClient supplierClient;

    public ApiResponse<List<AggregatedItemDTO>> getAggregatedItems(String ip, String userAgent, String loginId) {
        ApiResponse<List<ItemDTO>> itemResponse = itemClient.getItemList(ip, userAgent, loginId).block();
        ApiResponse<List<CategoryDTO>> categoryResponse = categoryClient.getCategoryList(ip, userAgent, loginId).block();
        ApiResponse<List<SupplierDTO>> supplierResponse = supplierClient.getSupplierList(ip, userAgent, loginId).block();

        if (itemResponse == null || categoryResponse == null || !itemResponse.isSuccess() || !categoryResponse.isSuccess()) {
            ApiResponse.fail("Failed to retrieve item or category data");
        }

        List<ItemDTO> items = itemResponse.getData();
        List<CategoryDTO> categories = categoryResponse.getData();
        List<SupplierDTO> suppliers = supplierResponse.getData();

        Map<Integer, String> categoryMap = categories
                .stream()
                .collect(Collectors.toMap(CategoryDTO::getId, CategoryDTO::getName));

        Map<Integer, String> supplierMap = suppliers
                .stream()
                .collect(Collectors.toMap(SupplierDTO::getId, SupplierDTO::getName));

        List<AggregatedItemDTO> resultDTOList = items
                .stream()
                .map(item -> {
                   String categoryName = categoryMap.getOrDefault(item.getCategoryId(), "미지정");
                   String supplierName = supplierMap.getOrDefault(item.getDefaultSupplierId(), "미지정");
                   return new AggregatedItemDTO(
                           item.getItemId(),
                           item.getCode(),
                           item.getName(),
                           categoryName,
                           item.getDescription(),
                           item.getUnit(),
                           item.getCost(),
                           item.getPrice(),
                           item.getIsLotTracked(),
                           supplierName
                   );
                }).collect(Collectors.toList());

        return ApiResponse.success(resultDTOList);
    }

    public ApiResponse<ItemFormInitDTO> getFormInitData(String ip, String userAgent, String loginId){
        ApiResponse<List<CategoryDTO>> categoryResponse = categoryClient.getCategoryList(ip, userAgent, loginId).block();
        ApiResponse<List<SupplierDTO>> supplierResponse = supplierClient.getSupplierList(ip, userAgent, loginId).block();

        if (categoryResponse == null || supplierResponse == null || !categoryResponse.isSuccess() || !supplierResponse.isSuccess()) {
            return ApiResponse.fail("카테고리 또는 공급업체 데이터를 가져오지 못했습니다.");
        }
        return ApiResponse.success(
                new ItemFormInitDTO(
                        categoryResponse.getData(),
                        supplierResponse.getData()
                )
        );
    }

    public ApiResponse<Long> createItem(ItemDTO itemDTO, String ip, String userAgent, String loginId){
        ApiResponse<Long> response = itemClient.createItem(itemDTO, ip, userAgent, loginId).block();
        if(!response.isSuccess() || response == null) return ApiResponse.fail("관리자에게 문의바랍니다.");
        return response;
    }
}
