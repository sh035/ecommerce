package com.ecommerce.category.service;

import com.ecommerce.category.domain.dto.CategoryDto;
import com.ecommerce.category.domain.dto.ChildCategoryDto;
import com.ecommerce.category.domain.dto.ParentCategoryDto;
import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.exception.DuplicateCategoryException;
import com.ecommerce.category.exception.NotExistParentException;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public ParentCategoryDto parentCategoryCreate(ParentCategoryDto dto) {
        if (categoryRepository.existsByCategoryName(dto.getCategoryName())) {
            throw new DuplicateCategoryException("이미 존재하는 카테고리입니다.");
        }

        categoryRepository.save(Category.builder()
                .categoryName(dto.getCategoryName())
            .build());

        return ParentCategoryDto.from(dto.getCategoryName());
    }

    public ChildCategoryDto childCategoryCreate(ChildCategoryDto dto) {
        if (!categoryRepository.existsById(dto.getParentId())) {
            throw new NotExistParentException("부모 카테고리가 존재하지 않습니다.");
        }

        Category category = categoryRepository.save(Category.builder()
            .categoryName(dto.getCategoryName())
            .parentId(dto.getParentId())
            .build());

        return ChildCategoryDto.from(category);
    }

    //TODO 업데이트시 redis 값 삭제되는지 확인, 부모 카테고리 업데이트 추가해야됨
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDto update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        category.update(dto.getCategoryName(), dto.getParentId());

        return CategoryDto.from(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        category.delete(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "parentCategories", key = "'all'", cacheManager = "redisCacheManager")
    public List<CategoryDto> getParentCategories() {
        return categoryRepository.findByParentIdIsNull().stream()
            .map(CategoryDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "childCategories", key = "#parentId", cacheManager ="redisCacheManager")
    public List<CategoryDto> getChildCategories(Long parentId) {
        log.info("service parentId: {}", parentId);
        return categoryRepository.findByParentId(parentId).stream()
            .map(CategoryDto::from)
            .collect(Collectors.toList());
    }

}
