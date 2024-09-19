package com.ecommerce.category.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ParentCategoryDto {

    private String categoryName;

    public static ParentCategoryDto from(String categoryName) {
        return ParentCategoryDto.builder()
            .categoryName(categoryName)
            .build();
    }
}
