package kr.cms.categoryService.service;


import kr.cms.categoryService.dto.CategoryDTO;
import kr.cms.common.dto.ApiResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse<CategoryDTO> getCategoryById(Long id, String ip, String userAgent, String loginId);

    ApiResponse<List<CategoryDTO>> getAllCategories(String ip, String userAgent, String loginId);

    ApiResponse<String> createCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> updateCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> deleteCategory(Long id, String ip, String userAgent, String loginId);
}
