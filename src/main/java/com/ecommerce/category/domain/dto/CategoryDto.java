package com.ecommerce.category.domain.dto;

import com.ecommerce.category.domain.entity.Category;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
  private Long id;
  private String name;
  private Long parentId;
  private List<CategoryDto> children;

  public static CategoryDto from(Category category) {

    return CategoryDto.builder()
        .id(category.getId())
        .name(category.getName())
        .parentId(category.getParentId())
        .children(category.getChildren() != null ? category.getChildren().stream()
            .map(CategoryDto::from).collect(Collectors.toList()) : null)
        .build();
  }

  public boolean hasParent() {
    return parentId != null;
  }
}
