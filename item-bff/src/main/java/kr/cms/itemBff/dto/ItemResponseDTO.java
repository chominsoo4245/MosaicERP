
package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    private Long itemId;           // 품목 ID
    private String code;           // 품목 코드
    private String name;           // 품목 명
    private String description;    // 품목 설명
    private String unit;           // 단위
    private BigDecimal cost;       // 원가
    private BigDecimal price;      // 판매 가격
    private Boolean isLotTracked;  // LOT 관리여부

    // 카테고리 정보
    private Integer categoryId;    // 카테고리 ID
    private String categoryName;   // 카테고리 명

    // 공급업체 정보 (선택적)
    private Integer supplierId;    // 공급업체 ID
    private String supplierName;   // 공급업체 명
}