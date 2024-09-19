package com.ecommerce.product.domain.dto;

import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponseDto {

    private Long id;
    private String parentCategoryName;
    private String childCategoryName;
    private String name;

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .parentCategoryName(product.getParentCategory().getCategoryName())
            .childCategoryName(product.getChildCategory().getCategoryName())
            .name(product.getName())
            .build();
    }
}
