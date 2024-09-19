package com.ecommerce.category.service;

import com.ecommerce.category.domain.dto.CategoryDto;
import com.ecommerce.category.domain.dto.ChildCategoryDto;
import com.ecommerce.category.domain.dto.ParentCategoryDto;
import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.exception.DuplicatedCategoryException;
import com.ecommerce.category.exception.NotExistParentException;
import com.ecommerce.category.repository.CategoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @CacheEvict(value = "parentCategories", allEntries = true, cacheManager = "redisCacheManager")
    public ParentCategoryDto parentCategoryCreate(ParentCategoryDto dto) {
        if (categoryRepository.existsByCategoryName(dto.getCategoryName())) {
            throw new DuplicatedCategoryException();
        }

        categoryRepository.save(Category.builder()
            .categoryName(dto.getCategoryName())
            .build());

        return ParentCategoryDto.from(dto.getCategoryName());
    }

    @CacheEvict(value = "childCategories", key = "#dto.parentId", cacheManager = "redisCacheManager")
    public ChildCategoryDto childCategoryCreate(ChildCategoryDto dto) {
        if (!categoryRepository.existsById(dto.getParentId())) {
            throw new NotExistParentException();
        }

        if (categoryRepository.existsByCategoryName(dto.getCategoryName())) {
            throw new DuplicatedCategoryException();
        }

        Category category = categoryRepository.save(Category.builder()
            .categoryName(dto.getCategoryName())
            .parentId(dto.getParentId())
            .build());

        return ChildCategoryDto.from(category);
    }

    @CacheEvict(value = "parentCategories", allEntries = true, cacheManager = "redisCacheManager")
    public CategoryDto updateParentCategory(Long id, ParentCategoryDto dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        category.parentUpdate(dto.getCategoryName());

        return CategoryDto.from(categoryRepository.save(category));
    }

    @CacheEvict(value = "childCategories", key = "#dto.parentId", cacheManager = "redisCacheManager")
    public CategoryDto updateChildCategory(Long id, ChildCategoryDto dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        category.childUpdate(dto.getCategoryName(), dto.getParentId());

        return CategoryDto.from(categoryRepository.save(category));
    }

    // TODO 캐시 삭제 (임시방편)
    @Caching(evict = {
        @CacheEvict(value = "parentCategories", allEntries = true, cacheManager = "redisCacheManager"),
        @CacheEvict(value = "childCategories", allEntries = true, cacheManager = "redisCacheManager")
    })
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        if (category.getParentId() == null) {
            categoryRepository.findByParentId(id).stream()
                .forEach(child -> child.delete(LocalDateTime.now()));
        }

        category.delete(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "parentCategories", key = "'all'", cacheManager = "redisCacheManager")
    public List<CategoryDto> getParentCategories() {
        List<Category> categories = categoryRepository.findByParentIdIsNull();
        return categories.stream()
            .map(CategoryDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "childCategories", key = "#parentId", cacheManager ="redisCacheManager")
    public List<CategoryDto> getChildCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
            .map(CategoryDto::from)
            .collect(Collectors.toList());
    }

}
