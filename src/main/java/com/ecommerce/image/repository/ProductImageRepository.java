package com.ecommerce.image.repository;

import com.ecommerce.image.domain.entity.ProductImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

  Optional<List<ProductImage>> findByProductId(Long productId);
  void deleteByProductId(Long productId);
}
