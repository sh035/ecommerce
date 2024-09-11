package com.ecommerce.product.domain.dto;

import com.ecommerce.product.domain.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
  private Long categoryId;
  private String name;
  private int price;
  private String description;
  private int deliveryCharge;
  private int qty;
}
