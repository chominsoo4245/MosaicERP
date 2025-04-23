package kr.cms.itemBff.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.client.*;
import kr.cms.itemBff.dto.*;
import kr.cms.itemBff.logging.LogSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemBFFService {
    // try-confirm-cancel
    private final ItemClient itemClient;
    private final LotClient lotClient;
    private final SerialClient serialClient;
    private final InventoryClient inventoryClient;
    // ReadOnly
    private final CategoryClient categoryClient;
    private final WarehouseClient warehouseClient;
    private final BinClient binClient;
    private final SupplierClient supplierClient;
    // log
    private final LogSender logSender;

    public Mono<ApiResponse<List<ItemResponseDTO>>> getItemList(String ip, String userAgent, String loginId) {
        logSender.sendLog("GET_ITEM_LIST_START", "Fetching item list", ip, userAgent, loginId);

        Mono<ApiResponse<List<ItemDTO>>> itemsMono = itemClient.getItemList(ip, userAgent, loginId);
        Mono<ApiResponse<List<CategoryDTO>>> categoriesMono = categoryClient.getCategoryList(ip, userAgent, loginId);
        Mono<ApiResponse<List<SupplierDTO>>> suppliersMono = supplierClient.getSupplierList(ip, userAgent, loginId);

        return Mono.zip(itemsMono, categoriesMono, suppliersMono)
                .flatMap(tuple -> {
                    ApiResponse<List<ItemDTO>> itemsResponse = tuple.getT1();
                    ApiResponse<List<CategoryDTO>> categoriesResponse = tuple.getT2();
                    ApiResponse<List<SupplierDTO>> suppliersResponse = tuple.getT3();

                    if (!itemsResponse.isSuccess() || !categoriesResponse.isSuccess() || !suppliersResponse.isSuccess()) {
                        logSender.sendLog("GET_ITEM_LIST_ERROR", "Failed to retrieve basic data", ip, userAgent, loginId);
                        return Mono.just(ApiResponse.fail("Failed to retrieve item, category, or supplier data"));
                    }

                    List<ItemDTO> items = itemsResponse.getData();
                    if (items.isEmpty()) {
                        logSender.sendLog("GET_ITEM_LIST_EMPTY", "No items found", ip, userAgent, loginId);
                        return Mono.just(ApiResponse.success(List.of()));
                    }

                    List<CategoryDTO> categories = categoriesResponse.getData();
                    List<SupplierDTO> suppliers = suppliersResponse.getData();

                    Map<Integer, String> categoryMap = categories.stream()
                            .collect(Collectors.toMap(CategoryDTO::getId, CategoryDTO::getName, (a, b) -> a));

                    Map<Integer, String> supplierMap = suppliers.stream()
                            .collect(Collectors.toMap(SupplierDTO::getId, SupplierDTO::getName, (a, b) -> a));

                    List<ItemResponseDTO> resultList = items.stream()
                            .map(item -> {
                                String categoryName = categoryMap.getOrDefault(item.getCategoryId(), "미지정");
                                String supplierName = supplierMap.getOrDefault(item.getDefaultSupplierId(), "미지정");

                                return ItemResponseDTO.builder()
                                        .itemId(item.getItemId())
                                        .code(item.getCode())
                                        .name(item.getName())
                                        .description(item.getDescription())
                                        .unit(item.getUnit())
                                        .cost(item.getCost())
                                        .price(item.getPrice())
                                        .isLotTracked(item.getIsLotTracked())
                                        .categoryId(item.getCategoryId())
                                        .categoryName(categoryName)
                                        .supplierId(item.getDefaultSupplierId())
                                        .supplierName(supplierName)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    logSender.sendLog("GET_ITEM_LIST_SUCCESS", "Successfully retrieved " + resultList.size() + " items", ip, userAgent, loginId);
                    return Mono.just(ApiResponse.success(resultList));
                });
    }


    public Mono<ApiResponse<FormDataInitDTO>> getFormDataInit(String ip, String userAgent, String loginId) {
        logSender.sendLog("GET_FORM_DATA_INIT", "Fetching form initialization data", ip, userAgent, loginId);

        return Mono.zip(
                categoryClient.getCategoryList(ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("GET_CATEGORIES_ERROR", "Error fetching categories: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Error"));
                        }),
                warehouseClient.getWarehouseList(ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("GET_WAREHOUSES_ERROR", "Error fetching warehouses: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Error"));
                        }),
                binClient.getBinList(ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("GET_BINS_ERROR", "Error fetching bins: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Error"));
                        }),
                supplierClient.getSupplierList(ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("GET_SUPPLIERS_ERROR", "Error fetching suppliers: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Error"));
                        })
        ).map(tuple -> {
            List<CategoryDTO> categories = tuple.getT1().getData();
            List<WarehouseDTO> warehouses = tuple.getT2().getData();
            List<BinDTO> bins = tuple.getT3().getData();
            List<SupplierDTO> suppliers = tuple.getT4().getData();

            logSender.sendLog("GET_FORM_DATA_INIT_SUCCESS", "Successfully fetched form initialization data", ip, userAgent, loginId);

            return ApiResponse.success(new FormDataInitDTO(categories, warehouses, bins, suppliers));
        });
    }

    public Mono<ApiResponse<ItemDetailDTO>> getItemDetail(Long itemId, String ip, String userAgent, String loginId) {
        return itemClient.getItem(itemId, ip, userAgent, loginId).flatMap(itemResponse -> {
            if(!itemResponse.isSuccess() || itemResponse.getData() == null) {
                return Mono.just(ApiResponse.fail("Item not found"));
            }

            ItemDTO item = itemResponse.getData();

            Mono<ApiResponse<CategoryDTO>> categoryMono = categoryClient.getCategory(item.getCategoryId(), ip, userAgent, loginId)
                    .onErrorResume(e -> {
                        logSender.sendLog("GET_CATEGORY_ERROR", "Error fetching category: " + e.getMessage(), ip, userAgent, loginId);
                        return Mono.just(ApiResponse.success(new CategoryDTO()));
                    });

            Mono<ApiResponse<SupplierDTO>> supplierMono = item.getDefaultSupplierId() != null ?
                    supplierClient.getSupplier(item.getDefaultSupplierId(), ip, userAgent, loginId)
                            .onErrorResume(e -> {
                                logSender.sendLog("GET_SUPPLIER_ERROR", "Error fetching supplier: " + e.getMessage(), ip, userAgent, loginId);
                                return Mono.just(ApiResponse.success(new SupplierDTO()));
                            }) :
                    Mono.just(ApiResponse.success(new SupplierDTO()));

            return Mono.zip(categoryMono, supplierMono).map(tuple -> {
                System.out.println(tuple.getT1().getData());
               CategoryDTO category = tuple.getT1().getData();
               SupplierDTO supplier = tuple.getT2().getData();
               ItemDetailDTO itemDetailDTO = ItemDetailDTO.builder()
                       .itemId(item.getItemId())
                       .code(item.getCode())
                       .description(item.getDescription())
                       .unit(item.getUnit())
                       .cost(item.getCost())
                       .price(item.getPrice())
                       .isLotTracked(item.getIsLotTracked())
                       .createdAt(item.getCreatedAt())
                       .updatedAt(item.getUpdatedAt())
                       .categoryId(item.getCategoryId())
                       .categoryName(category != null ? category.getName() : "없음")
                       .supplierId(item.getDefaultSupplierId())
                       .supplierName(supplier != null ? supplier.getName() : "없음")
                       .build();
               return ApiResponse.success(itemDetailDTO);
            });
        });
    }

    public Mono<ApiResponse<CreateItemResponse>> createItem(CreateItemRequest request, String ip, String userAgent, String loginId) {
        String transactionId = UUID.randomUUID().toString();
        logSender.sendLog("CREATE_ITEM_START", "Starting item creation saga with transaction ID: " + transactionId, ip, userAgent, loginId);

        return itemClient.tryCreateItem(transactionId, request.toItemDTO(), ip, userAgent, loginId)
                .flatMap(itemResponse -> {
                    logSender.sendLog("ITEM_TRY_SUCCESS", "Item creation prepared with transaction ID: " + transactionId, ip, userAgent, loginId);

                    if (Boolean.TRUE.equals(request.getIsLotTracked())) {
                        return lotClient.tryCreateLot(transactionId, request.toLotDTO(), ip, userAgent, loginId)
                                .flatMap(lotResponse -> {
                                    logSender.sendLog("LOT_TRY_SUCCESS", "Lot creation prepared with transaction ID: " + transactionId, ip, userAgent, loginId);

                                    return serialClient.tryCreateSerial(transactionId, request.toSerialDTO(), ip, userAgent, loginId)
                                            .flatMap(serialResponse -> {
                                                logSender.sendLog("SERIAL_TRY_SUCCESS", "Serial creation prepared with transaction ID: " + transactionId, ip, userAgent, loginId);

                                                if (request.getWarehouseId() != null) {
                                                    return inventoryClient.tryCreateInventory(transactionId, request.toInventoryDTO(), ip, userAgent, loginId)
                                                            .flatMap(inventoryResponse -> {
                                                                logSender.sendLog("INVENTORY_TRY_SUCCESS", "Inventory creation prepared with transaction ID: " + transactionId, ip, userAgent, loginId);

                                                                return confirmAllServices(transactionId, request, ip, userAgent, loginId)
                                                                        .map(response -> ApiResponse.success(response));
                                                            });
                                                } else {
                                                    return confirmAllServices(transactionId, request, ip, userAgent, loginId)
                                                            .map(response -> ApiResponse.success(response));
                                                }
                                            })
                                            .onErrorResume(e -> {
                                                logSender.sendLog("SERIAL_TRY_ERROR", "Error in Serial try phase: " + e.getMessage(), ip, userAgent, loginId);
                                                return cancelAllServices(transactionId, ip, userAgent, loginId)
                                                        .then(Mono.just(ApiResponse.fail("Failed to prepare Serial creation: " + e.getMessage())));
                                            });
                                })
                                .onErrorResume(e -> {
                                    logSender.sendLog("LOT_TRY_ERROR", "Error in Lot try phase: " + e.getMessage(), ip, userAgent, loginId);
                                    return cancelAllServices(transactionId, ip, userAgent, loginId)
                                            .then(Mono.just(ApiResponse.fail("Failed to prepare Lot creation: " + e.getMessage())));
                                });
                    } else {
                        if (request.getWarehouseId() != null) {
                            return inventoryClient.tryCreateInventory(transactionId, request.toInventoryDTO(), ip, userAgent, loginId)
                                    .flatMap(inventoryResponse -> {
                                        logSender.sendLog("INVENTORY_TRY_SUCCESS", "Inventory creation prepared with transaction ID: " + transactionId, ip, userAgent, loginId);

                                        // Item과 Inventory만 Confirm
                                        return confirmItemAndInventory(transactionId, request, ip, userAgent, loginId)
                                                .map(response -> ApiResponse.success(response));
                                    })
                                    .onErrorResume(e -> {
                                        logSender.sendLog("INVENTORY_TRY_ERROR", "Error in Inventory try phase: " + e.getMessage(), ip, userAgent, loginId);
                                        return cancelAllServices(transactionId, ip, userAgent, loginId)
                                                .then(Mono.just(ApiResponse.fail("Failed to prepare Inventory creation: " + e.getMessage())));
                                    });
                        } else {
                            return confirmItemOnly(transactionId, ip, userAgent, loginId)
                                    .map(response -> ApiResponse.success(response));
                        }
                    }
                })
                .onErrorResume(e -> {
                    logSender.sendLog("ITEM_TRY_ERROR", "Error in Item try phase: " + e.getMessage(), ip, userAgent, loginId);
                    return cancelAllServices(transactionId, ip, userAgent, loginId)
                            .then(Mono.just(ApiResponse.fail("Failed to prepare Item creation: " + e.getMessage())));
                });
    }

    private Mono<CreateItemResponse> confirmAllServices(String transactionId, CreateItemRequest request,
                                                        String ip, String userAgent, String loginId) {
        return itemClient.confirmCreateItem(transactionId, ip, userAgent, loginId)
                .flatMap(itemResponse -> {
                    Long itemId = extractId(itemResponse.getData());
                    logSender.sendLog("ITEM_CONFIRM_SUCCESS", "Item confirmed with ID: " + itemId, ip, userAgent, loginId);

                    LotDTO lotDTO = request.toLotDTO();
                    lotDTO.setItemId(itemId);
                    lotDTO.setLotNumber(generateLotNumber(itemId));

                    return lotClient.confirmCreateLot(transactionId, lotDTO, ip, userAgent, loginId)
                            .flatMap(lotResponse -> {
                                Long lotId = extractId(lotResponse.getData());
                                String lotNumber = lotDTO.getLotNumber();
                                logSender.sendLog("LOT_CONFIRM_SUCCESS", "Lot confirmed with ID: " + lotId + " and number: " + lotNumber, ip, userAgent, loginId);

                                SerialDTO serialDTO = request.toSerialDTO();
                                serialDTO.setItemId(itemId);
                                serialDTO.setLotId(lotId);
                                serialDTO.setSerialNumber(generateSerialNumber(itemId, lotId));

                                return serialClient.confirmCreateSerial(transactionId, serialDTO, ip, userAgent, loginId)
                                        .flatMap(serialResponse -> {
                                            Long serialId = extractId(serialResponse.getData());
                                            String serialNumber = serialDTO.getSerialNumber();
                                            logSender.sendLog("SERIAL_CONFIRM_SUCCESS", "Serial confirmed with ID: " + serialId + " and number: " + serialNumber, ip, userAgent, loginId);

                                            if (request.getWarehouseId() != null) {
                                                InventoryDTO inventoryDTO = request.toInventoryDTO();
                                                inventoryDTO.setItemId(itemId);
                                                inventoryDTO.setLotId(lotId.intValue());

                                                return inventoryClient.confirmCreateInventory(transactionId, inventoryDTO, ip, userAgent, loginId)
                                                        .map(inventoryResponse -> {
                                                            Long inventoryId = extractId(inventoryResponse.getData());
                                                            logSender.sendLog("INVENTORY_CONFIRM_SUCCESS", "Inventory confirmed with ID: " + inventoryId, ip, userAgent, loginId);

                                                            logSender.sendLog("CREATE_ITEM_SUCCESS", "Successfully created item with ID: " + itemId, ip, userAgent, loginId);

                                                            return new CreateItemResponse(
                                                                    itemId, lotId, lotNumber, serialId, serialNumber, inventoryId);
                                                        });
                                            } else {
                                                logSender.sendLog("CREATE_ITEM_SUCCESS", "Successfully created item with ID: " + itemId + " (without inventory)", ip, userAgent, loginId);

                                                return Mono.just(new CreateItemResponse(
                                                        itemId, lotId, lotNumber, serialId, serialNumber, null));
                                            }
                                        });
                            });
                });
    }

    private Mono<CreateItemResponse> confirmItemAndInventory(String transactionId, CreateItemRequest request,
                                                             String ip, String userAgent, String loginId) {
        return itemClient.confirmCreateItem(transactionId, ip, userAgent, loginId)
                .flatMap(itemResponse -> {
                    Long itemId = extractId(itemResponse.getData());
                    logSender.sendLog("ITEM_CONFIRM_SUCCESS", "Item confirmed with ID: " + itemId, ip, userAgent, loginId);

                    InventoryDTO inventoryDTO = request.toInventoryDTO();
                    inventoryDTO.setItemId(itemId);

                    return inventoryClient.confirmCreateInventory(transactionId, inventoryDTO, ip, userAgent, loginId)
                            .map(inventoryResponse -> {
                                Long inventoryId = extractId(inventoryResponse.getData());
                                logSender.sendLog("INVENTORY_CONFIRM_SUCCESS", "Inventory confirmed with ID: " + inventoryId, ip, userAgent, loginId);

                                logSender.sendLog("CREATE_ITEM_SUCCESS", "Successfully created item with ID: " + itemId + " and inventory", ip, userAgent, loginId);

                                return new CreateItemResponse(itemId, null, null, null, null, inventoryId);
                            });
                });
    }

    private Mono<CreateItemResponse> confirmItemOnly(String transactionId, String ip, String userAgent, String loginId) {
        return itemClient.confirmCreateItem(transactionId, ip, userAgent, loginId)
                .map(itemResponse -> {
                    Long itemId = extractId(itemResponse.getData());
                    logSender.sendLog("ITEM_CONFIRM_SUCCESS", "Item confirmed with ID: " + itemId, ip, userAgent, loginId);

                    logSender.sendLog("CREATE_ITEM_SUCCESS", "Successfully created item with ID: " + itemId + " (basic item only)", ip, userAgent, loginId);

                    return new CreateItemResponse(itemId, null, null, null, null, null);
                });
    }

    private Mono<Void> cancelAllServices(String transactionId, String ip, String userAgent, String loginId) {
        logSender.sendLog("CANCEL_ITEM_CREATION", "Cancelling all services for transaction ID: " + transactionId, ip, userAgent, loginId);

        return Mono.zip(
                itemClient.cancelCreateItem(transactionId, ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("CANCEL_ITEM_ERROR", "Error cancelling item: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Warning: Item cancellation failed"));
                        }),
                lotClient.cancelCreateLot(transactionId, ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("CANCEL_LOT_ERROR", "Error cancelling lot: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Warning: Lot cancellation failed"));
                        }),
                serialClient.cancelCreateSerial(transactionId, ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("CANCEL_SERIAL_ERROR", "Error cancelling serial: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Warning: Serial cancellation failed"));
                        }),
                inventoryClient.cancelCreateInventory(transactionId, ip, userAgent, loginId)
                        .onErrorResume(e -> {
                            logSender.sendLog("CANCEL_INVENTORY_ERROR", "Error cancelling inventory: " + e.getMessage(), ip, userAgent, loginId);
                            return Mono.just(ApiResponse.fail("Warning: Inventory cancellation failed"));
                        })
        ).then(Mono.fromRunnable(() -> {
            logSender.sendLog("CANCEL_ITEM_CREATION_COMPLETE", "Item creation cancelled successfully", ip, userAgent, loginId);
        }));
    }

    private Long extractId(String idString) {
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            logSender.sendLog("PARSE_ID_ERROR", "Error parsing ID: " + idString, null, null, null);
            throw new RuntimeException("Failed to parse ID: " + idString);
        }
    }

    private String generateLotNumber(Long itemId) {
        return "LOT-" + itemId + "-" + System.currentTimeMillis();
    }

    private String generateSerialNumber(Long itemId, Long lotId) {
        return "SER-" + itemId + "-" + lotId + "-" + System.currentTimeMillis();
    }
}