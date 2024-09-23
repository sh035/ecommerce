package com.ecommerce.product.domain.dto;

import com.ecommerce.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResDto {

    private Long id;
    private String parentCategoryName;
    private String childCategoryName;
    private String title;

    public static ProductResDto from(Product product) {
        return ProductResDto.builder()
            .id(product.getId())
            .parentCategoryName(product.getParentCategory().getCategoryName())
            .childCategoryName(product.getChildCategory().getCategoryName())
            .title(product.getTitle())
            .build();
    }
}
