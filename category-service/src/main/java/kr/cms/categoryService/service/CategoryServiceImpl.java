package kr.cms.categoryService.service;


import kr.cms.categoryService.dto.CategoryDTO;
import kr.cms.categoryService.entity.Category;
import kr.cms.categoryService.logging.LogSender;
import kr.cms.categoryService.repository.CategoryRepository;
import kr.cms.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final LogSender logSender;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<CategoryDTO> getCategoryById(Long id, String ip, String userAgent, String loginId) {
        try {
            Category entity = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            logSender.sendLog("GET_CATEGORY_SUCCESS", "Category retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(convertToDTO(entity));
        } catch (Exception e) {
            logSender.sendLog("GET_CATEGORY_FAIL", "Failed to retrieve category: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieve category: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<CategoryDTO>> getAllCategories(String ip, String userAgent, String loginId) {
        try {
            List<Category> entityList = categoryRepository.findAll();
            List<CategoryDTO> dtoList = entityList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logSender.sendLog("GET_ALL_CATEGORIES_SUCCESS", "All categories retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("GET_ALL_CATEGORIES_FAIL", "Failed to retrieve categories: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to retrieve categories: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public ApiResponse<String> createCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId) {
        try {
            Category entity = convertToEntity(categoryDTO);
            categoryRepository.save(entity);
            logSender.sendLog("CREATE_CATEGORY_SUCCESS", "Category created successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Category created successfully");
        } catch (Exception e) {
            logSender.sendLog("CREATE_CATEGORY_", "Failed to create category: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to create category: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> updateCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId) {
        try {
            categoryRepository.findById(categoryDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Category entity = convertToEntity(categoryDTO);
            categoryRepository.save(entity);
            logSender.sendLog("UPDATE_CATEGORY_SUCCESS","Category updated successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Category updated successfully");
        } catch (Exception e) {
            logSender.sendLog("UPDATE_CATEGORY_FAIL","Failed to update category: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to update category: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteCategory(Long id, String ip, String userAgent, String loginId) {
        try {
            if (!categoryRepository.existsById(id)) {
                logSender.sendLog("DELETE_CATEGORY_FAIL", "Category not found for deletion", ip, userAgent, loginId);
            }
            categoryRepository.deleteById(id);
            logSender.sendLog("DELETE_CATEGORY_SUCCESS", "Category deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Category deleted successfully");
        } catch (Exception e) {
            logSender.sendLog("DELETE_CATEGORY_FAIL", "Failed to delete category: " + e.getMessage(), ip, userAgent, loginId);
            return ApiResponse.fail("Failed to delete category: " + e.getMessage());
        }
    }

    private CategoryDTO convertToDTO(Category entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setParentId(entity.getParentId());
        dto.setCategoryType(entity.getCategoryType());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setLevel(entity.getLevel());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setParentId(dto.getParentId());
        entity.setCategoryType(dto.getCategoryType());
        entity.setCategoryCode(dto.getCategoryCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLevel(dto.getLevel());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
