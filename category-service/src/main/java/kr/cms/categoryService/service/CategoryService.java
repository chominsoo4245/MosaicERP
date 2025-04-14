package kr.cms.categoryService.service;


import kr.cms.categoryService.dto.CategoryDTO;
import kr.cms.categoryService.dto.SearchDataDTO;
import kr.cms.common.dto.ApiResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse<CategoryDTO> getCategoryById(Long id, String ip, String userAgent, String loginId);

    ApiResponse<List<CategoryDTO>> getAllCategories(String ip, String userAgent, String loginId);

    ApiResponse<List<CategoryDTO>> getSearchCategories(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId);

    ApiResponse<Long> createCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> updateCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId);

    ApiResponse<String> deleteCategory(Long id, String ip, String userAgent, String loginId);
}
