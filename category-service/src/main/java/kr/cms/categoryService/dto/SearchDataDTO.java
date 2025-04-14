package kr.cms.categoryService.dto;

import lombok.Data;

@Data
public class SearchDataDTO {
    private String keyword;
    private String categoryType;
    private String categoryCode;
    private Integer level;
}
