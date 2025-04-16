package kr.cms.categoryService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "parent_category_id")
    private Long parentId;

    private String categoryType;
    private String categoryCode;
    private String shortCode;
    private String name;
    private String description;
    private Integer level;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
