package com.ecommerce.product.service;

import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductResponse;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import com.ecommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public ProductResponse createProduct(ProductCreateDto dto) {
    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    return ProductResponse.from(
        productRepository.save(Product.builder()
            .category(category)
            .name(dto.getName())
            .price(dto.getPrice())
            .description(dto.getDescription())
            .deliveryCharge(dto.getDeliveryCharge())
            .qty(dto.getQty())
            .productStatus(ProductStatus.SELL)
            .build()));
  }

  @Transactional
  public ProductResponse updateProduct(Long id, ProductUpdateDto dto) {
    Product product = getProduct(id);
    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    product.update(dto, category);
    return ProductResponse.from(productRepository.save(product));

  }

  public ProductResponse detail(Long id) {
    Product product = getProduct(id);

    return ProductResponse.from(product);
  }

  @Transactional
  public ProductResponse delete(Long id) {
    Product product = getProduct(id);
    productRepository.delete(product);

    return ProductResponse.from(product);
  }

  private Product getProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    return product;
  }
}
