package com.ecommerce.product.domain.entity;

import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.global.entity.BaseTime;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.domain.enums.ProductStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CATEGORY_ID")
  private Category category;

  private String name;
  private int price;
  private String description;
  private int deliveryCharge;
  private int qty;
  private ProductStatus productStatus;

  public void setProductStatus(ProductStatus productStatus) {
    this.productStatus = productStatus;
  }

  public void update(ProductUpdateDto dto, Category category) {
    this.category = category;
    this.name = dto.getName();
    this.price = dto.getPrice();
    this.description = dto.getDescription();
    this.deliveryCharge = dto.getDeliveryCharge();
    this.qty = dto.getQty();
    this.productStatus = dto.getProductStatus();
  }
}
