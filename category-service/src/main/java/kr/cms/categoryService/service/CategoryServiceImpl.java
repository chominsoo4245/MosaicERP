package kr.cms.categoryService.service;


import jakarta.persistence.criteria.Predicate;
import kr.cms.categoryService.dto.CategoryDTO;
import kr.cms.categoryService.dto.SearchDataDTO;
import kr.cms.categoryService.entity.Category;
import kr.cms.categoryService.logging.LogSender;
import kr.cms.categoryService.repository.CategoryRepository;
import kr.cms.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            throw e;
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
            throw e;
        }

    }

    @Override
    public ApiResponse<List<CategoryDTO>> getSearchCategories(SearchDataDTO searchDataDTO, String ip, String userAgent, String loginId) {
        if (isAllFieldsNull(searchDataDTO)) {
            return getAllCategories(ip, userAgent, loginId);
        }

        try {
            Specification<Category> spec = createCategorySpecification(searchDataDTO);
            List<Category> entityList = categoryRepository.findAll(spec);
            List<CategoryDTO> dtoList = entityList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            logSender.sendLog("SEARCH_CATEGORIES_SUCCESS", "Categories retrieved successfully", ip, userAgent, loginId);
            return ApiResponse.success(dtoList);
        } catch (Exception e) {
            logSender.sendLog("SEARCH_CATEGORIES_FAIL", "Failed to search categories: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional
    public ApiResponse<Long> createCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId) {
        try {
            Category entity = convertToEntity(categoryDTO);

            String autoShortCode = (entity.getParentId() != null)
                    ? generateSubCategoryShortCode(entity.getParentId(), categoryDTO.getCategoryType())
                    : getTopCategoryCode(categoryDTO.getCategoryType());
            entity.setShortCode(autoShortCode);
            // 생성 시간
            LocalDateTime now = LocalDateTime.now();
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            Category saved = categoryRepository.save(entity);
            logSender.sendLog("CREATE_CATEGORY_SUCCESS", "Category created successfully", ip, userAgent, loginId);
            return ApiResponse.success(saved.getId());
        } catch (Exception e) {
            logSender.sendLog("CREATE_CATEGORY_FAIL", "Failed to create category: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> updateCategory(CategoryDTO categoryDTO, String ip, String userAgent, String loginId) {
        try {
            Category existing = categoryRepository.findById(categoryDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Category entity = convertToEntity(categoryDTO);
            entity.setShortCode(existing.getShortCode());
            entity.setUpdatedAt(LocalDateTime.now());
            categoryRepository.save(entity);
            logSender.sendLog("UPDATE_CATEGORY_SUCCESS", "Category updated successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Category updated successfully");
        } catch (Exception e) {
            logSender.sendLog("UPDATE_CATEGORY_FAIL", "Failed to update category: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteCategory(Long id, String ip, String userAgent, String loginId) {
        try {
            if (!categoryRepository.existsById(id)) {
                logSender.sendLog("DELETE_CATEGORY_FAIL", "Category not found for deletion", ip, userAgent, loginId);
                return ApiResponse.fail("Category not found for deletion");
            }
            categoryRepository.deleteById(id);
            logSender.sendLog("DELETE_CATEGORY_SUCCESS", "Category deleted successfully", ip, userAgent, loginId);
            return ApiResponse.successWithMessage("Category deleted successfully");
        } catch (Exception e) {
            logSender.sendLog("DELETE_CATEGORY_FAIL", "Failed to delete category: " + e.getMessage(), ip, userAgent, loginId);
            throw e;
        }
    }

    private CategoryDTO convertToDTO(Category entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setParentId(entity.getParentId());
        dto.setCategoryType(entity.getCategoryType());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setShortCode(entity.getShortCode());
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
        entity.setShortCode(dto.getShortCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLevel(dto.getLevel());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    private Specification<Category> createCategorySpecification(SearchDataDTO searchDataDTO) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (searchDataDTO.getKeyword() != null && !searchDataDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + searchDataDTO.getKeyword().trim() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(root.get("name"), keyword),
                        cb.like(root.get("description"), keyword)
                ));
            }

            if (searchDataDTO.getCategoryType() != null && !searchDataDTO.getCategoryType().trim().isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("categoryType"), searchDataDTO.getCategoryType()));
            }

            if (searchDataDTO.getCategoryCode() != null && !searchDataDTO.getCategoryCode().trim().isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("categoryCode"), searchDataDTO.getCategoryCode()));
            }

            if (searchDataDTO.getLevel() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("level"), searchDataDTO.getLevel()));
            }

            return predicate;
        };
    }

    private boolean isAllFieldsNull(SearchDataDTO dto) {
        return (dto.getKeyword() == null || dto.getKeyword().trim().isEmpty())
                && (dto.getCategoryType() == null || dto.getCategoryType().trim().isEmpty())
                && (dto.getCategoryCode() == null || dto.getCategoryCode().trim().isEmpty())
                && dto.getLevel() == null;
    }

    private String getTopCategoryCode(String categoryType) {
        switch (categoryType.toUpperCase()) {
            case "PRODUCT":
            case "아이템":
            case "제품":
                return "PROD";
            case "MATERIAL":
            case "원자재":
            case "재료":
                return "MAT";
            case "SERVICE":
            case "서비스":
                return "SERV";
            case "PART":
            case "부품":
                return "PART";
            case "ASSET":
            case "자산":
                return "ASST";
            case "HUMAN_RESOURCE":
            case "인사":
            case "HR":
                return "HR";
            case "FINANCE":
            case "재무":
                return "FIN";
            case "SALES":
            case "영업":
                return "SAL";
            case "PURCHASE":
            case "구매":
                return "PUR";
            case "INVENTORY":
            case "재고":
                return "INV";
            case "PRODUCTION":
            case "생산":
                return "PRODCT";
            case "LOGISTICS":
            case "물류":
                return "LOG";
            case "CUSTOMER":
            case "고객":
                return "CUST";
            case "SUPPLIER":
            case "공급업체":
                return "SUP";
            case "QUALITY":
            case "품질":
                return "QUAL";
            case "MAINTENANCE":
            case "유지보수":
                return "MAIN";
            case "IT":
            case "정보기술":
                return "IT";
            case "R&D":
            case "연구개발":
                return "RND";
            case "MARKETING":
            case "마케팅":
                return "MARK";
            case "ADMINISTRATION":
            case "관리":
                return "ADMIN";
            case "LEGAL":
            case "법무":
                return "LEGAL";
            case "CUSTOMS":
            case "관세":
                return "CUSTS";
            case "SECURITY":
            case "보안":
                return "SEC";
            case "LOGISTICS_PLANNING":
            case "물류계획":
                return "LOGPLAN";
            case "PROCUREMENT":
            case "조달":
                return "PROC";
            default:
                throw new IllegalArgumentException("Unknown category type: " + categoryType);
        }
    }

    private String generateSubCategoryShortCode(Long parentCategoryId, String parentCategoryType) {
        String parentCode = getTopCategoryCode(parentCategoryType);
        long childCount = categoryRepository.countByParentCategoryId(parentCategoryId);
        String sequence = String.format("%03d", childCount + 1); // 3자리 번호
        return parentCode + "-" + sequence;
    }
}
