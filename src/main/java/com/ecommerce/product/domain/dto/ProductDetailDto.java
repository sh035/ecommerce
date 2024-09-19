package com.ecommerce.product.domain.dto;

import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.product.domain.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ProductDetailDto {

    private String name;
    private int price;
    private String description;
    private String parentCategory;
    private String ChildCategory;
    private int deliveryCharge;
    private int qty;
    private List<String> images;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/seoul")
    private LocalDateTime createdAt;

    public static ProductDetailDto from(Product product) {
        return ProductDetailDto.builder()
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .parentCategory(product.getParentCategory().getCategoryName())
            .ChildCategory(product.getChildCategory().getCategoryName())
            .deliveryCharge(product.getDeliveryCharge())
            .qty(product.getQty())
            .images(product.getImages().stream().map(Image::getImageUrl).toList())
            .createdAt(product.getCreatedAt())
            .build();
    }
}
