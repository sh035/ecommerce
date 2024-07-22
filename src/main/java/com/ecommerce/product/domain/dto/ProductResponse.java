package com.ecommerce.product.domain.dto;

import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
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

  public static ProductResponse from(Product product) {
    return ProductResponse.builder()
        .categoryName(product.getCategory().getName())
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .qty(product.getQty())
        .productStatus(product.getProductStatus())
        .build();
  }
}
