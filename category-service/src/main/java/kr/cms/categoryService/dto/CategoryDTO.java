package kr.cms.categoryService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private Long parentId;
    private String categoryType;
    private String categoryCode;
    private String name;
    private String description;
    private Integer level;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
