package com.ecommerce.product.domain.dto;

import com.ecommerce.image.domain.entity.ProductImage;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

  private String categoryName;
  private String name;
  private int price;
  private String description;
  private int deliveryCharge;
  private int qty;
  private ProductStatus productStatus;
  private List<String> imageUrl;

  public static ProductResponse from(Product product, List<ProductImage> imageUrl) {
    return ProductResponse.builder()
        .categoryName(product.getCategory().getCategoryName())
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .qty(product.getQty())
        .productStatus(product.getProductStatus())
        .imageUrl(imageUrl.stream().map(ProductImage::getImageUrl).collect(Collectors.toList()))
        .build();
  }
}
