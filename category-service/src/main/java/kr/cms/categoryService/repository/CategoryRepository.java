package kr.cms.categoryService.repository;

import kr.cms.categoryService.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    long countByParentCategoryId(Long parentCategoryId);
}
