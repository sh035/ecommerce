package com.ecommerce.category.repository;

import com.ecommerce.category.domain.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findById(Long id);
}
