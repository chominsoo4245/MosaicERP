package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormDataInitDTO {
    private List<CategoryDTO> categories;
    private List<WarehouseDTO> warehouses;
    private List<BinDTO> bins;
    private List<SupplierDTO> suppliers;
}
