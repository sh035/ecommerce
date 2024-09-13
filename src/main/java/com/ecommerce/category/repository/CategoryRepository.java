package com.ecommerce.category.repository;

import com.ecommerce.category.domain.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    boolean existsById(Long id);

    List<Category> findByParentIdIsNull();

    List<Category> findByParentId(Long parentId);
}
