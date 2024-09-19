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
public class ChildCategoryDto {

    private String categoryName;
    private Long parentId;

    public static ChildCategoryDto from(Category category) {
        return ChildCategoryDto.builder()
            .categoryName(category.getCategoryName())
            .parentId(category.getParentId())
            .build();
    }
}
