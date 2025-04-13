package kr.cms.categoryService.controller;

import kr.cms.categoryService.dto.CategoryDTO;
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
        return categoryService.getCategoryById(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @GetMapping("/list")
    public ApiResponse<List<CategoryDTO>> getCategoryList() {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return categoryService.getAllCategories(headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PostMapping("/create")
    public ApiResponse<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return categoryService.createCategory(categoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @PutMapping("/update")
    public ApiResponse<String> editCategory(@RequestBody CategoryDTO categoryDTO) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return categoryService.updateCategory(categoryDTO, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeCategory(@PathVariable Long id) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return categoryService.deleteCategory(id, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }
}
