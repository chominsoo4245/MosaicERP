package kr.cms.itemBff.dto;

import java.util.List;

public record ItemFormInitDTO(
        List<CategoryDTO> categories,
        List<SupplierDTO> suppliers
) {}
