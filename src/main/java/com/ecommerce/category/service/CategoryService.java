package com.ecommerce.category.service;

import com.ecommerce.category.domain.dto.CategoryDto;
import com.ecommerce.category.domain.dto.CategoryUpdateDto;
import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;


  @CacheEvict(value = "categories", allEntries = true)
  public CategoryDto create(CategoryDto dto) {

    Category parent = Category.root();

    if (dto.hasParent()) {
      parent = findByParentId(dto.getParentId());
    }

    Category newCategory = Category.builder()
        .id(dto.getId())
        .name(dto.getName())
        .parentId(dto.getParentId())
        .build();

    if (parent.getName() != null && !parent.getName().equals("ROOT")) {
      parent.addChild(newCategory);
      log.info("parent children = {}", parent.getChildren());
      categoryRepository.save(parent);
    }

    return CategoryDto.from(categoryRepository.save(newCategory));
  }

  @CacheEvict(value = "categories", allEntries = true)
  public CategoryDto update(Long id, CategoryUpdateDto dto) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    category.update(dto);

    return CategoryDto.from(categoryRepository.save(category));
  }

  @Cacheable(value = "categories")
  public List<CategoryDto> findAll() {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .filter(category -> category.getParentId() == null)
        .map(CategoryDto::from)
        .collect(Collectors.toList());
  }

  public CategoryDto findById(Long id) {
    return categoryRepository.findById(id).map(CategoryDto::from)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
  }

  public CategoryDto delete(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    CategoryDto categoryDto = CategoryDto.from(category);
    categoryRepository.deleteById(id);

    return categoryDto;
  }

  private Category findByParentId(Long parentId) {
    return categoryRepository.findById(parentId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARENT_CATEGORY));
  }
}
