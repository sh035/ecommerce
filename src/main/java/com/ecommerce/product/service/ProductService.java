package com.ecommerce.product.service;

import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.image.domain.entity.ProductImage;
import com.ecommerce.image.repository.ProductImageRepository;
import com.ecommerce.image.service.S3Service;
import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductResponse;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import com.ecommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductImageRepository productImageRepository;
  private final S3Service s3Service;

  @Transactional
  public ProductResponse createProduct(ProductCreateDto dto, List<MultipartFile> images) {
    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    Product product = productRepository.save(Product.builder()
        .category(category)
        .name(dto.getName())
        .price(dto.getPrice())
        .description(dto.getDescription())
        .deliveryCharge(dto.getDeliveryCharge())
        .qty(dto.getQty())
        .productStatus(ProductStatus.SELL)
        .build());

    List<ProductImage> productImages = s3Service.upload(images).stream()
        .map(imageUrl -> ProductImage.builder()
            .product(product)
            .imageUrl(imageUrl)
            .uploadDate(LocalDateTime.now())
            .build())
        .collect(Collectors.toList());

    productImageRepository.saveAll(productImages);

    return ProductResponse.from(product, productImages);
  }

  @Transactional
  public ProductResponse updateProduct(Long id, ProductUpdateDto dto, List<MultipartFile> images) {
    Product product = getProduct(id);
    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    product.update(dto, category);

    List<ProductImage> productImages = null;
    if (images != null && !images.isEmpty()) {
      productImageRepository.deleteByProductId(product.getId());

      productImages = s3Service.upload(images).stream()
          .map(imageUrl -> ProductImage.builder()
              .product(product)
              .imageUrl(imageUrl)
              .uploadDate(LocalDateTime.now())
              .build())
          .collect(Collectors.toList());

      productImageRepository.saveAll(productImages);
    } else {
    productImages = productImageRepository.findByProductId(product.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }
    return ProductResponse.from(productRepository.save(product), productImages);

  }

  public ProductResponse detail(Long id) {
    Product product = getProduct(id);

    List<ProductImage> productImages = productImageRepository.findByProductId(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

    return ProductResponse.from(product, productImages);
  }

  @Transactional
  public ProductResponse delete(Long id) {
    Product product = getProduct(id);
    List<ProductImage> productImages = productImageRepository.findByProductId(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    productRepository.delete(product);

    return ProductResponse.from(product, productImages);
  }

  private Product getProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    return product;
  }
}
