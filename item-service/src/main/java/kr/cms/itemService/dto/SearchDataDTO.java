package kr.cms.itemService.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class SearchDataDTO {
    private String keyword;
    private LocalDateTime createStart;
    private LocalDateTime createEnd;
    private LocalDateTime updateStart;
    private LocalDateTime updateEnd;
    private Integer categoryId;
    private Integer supplierId;
}
