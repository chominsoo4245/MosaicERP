package kr.cms.categoryService.controller;

import kr.cms.categoryService.dto.CategoryDTO;
import kr.cms.categoryService.dto.SearchDataDTO;
import kr.cms.categoryService.service.CategoryService;
import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-service")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final HeaderProvider headerProvider;

    @GetMapping("/{id}")
    public ApiResponse<CategoryDTO> getCategory(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.getCategoryById(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve category: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<CategoryDTO>> getCategoryList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.getAllCategories(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to retrieve categories: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ApiResponse<List<CategoryDTO>> getSearchList(@RequestBody SearchDataDTO searchDataDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.getSearchCategories(searchDataDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to search categories: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ApiResponse<Long> addCategory(@RequestBody CategoryDTO categoryDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.createCategory(categoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to create category: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ApiResponse<String> editCategory(@RequestBody CategoryDTO categoryDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.updateCategory(categoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to update category: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeCategory(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        try {
            return categoryService.deleteCategory(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
        } catch (Exception e){
            return ApiResponse.fail("Failed to delete category: " + e.getMessage());
        }
    }
}
