package com.ecommerce.category.domain.dto;

import com.ecommerce.category.domain.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String categoryName;
    private Long parentId;

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
            .id(category.getId())
            .categoryName(category.getCategoryName())
            .parentId(category.getParentId())
            .build();
    }
}
