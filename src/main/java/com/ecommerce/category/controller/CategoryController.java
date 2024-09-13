package com.ecommerce.category.controller;

import com.ecommerce.category.domain.dto.CategoryDto;
import com.ecommerce.category.domain.dto.ChildCategoryDto;
import com.ecommerce.category.domain.dto.ParentCategoryDto;
import com.ecommerce.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create/parent")
    public ResponseEntity<ParentCategoryDto> createParentCategory(
        @RequestBody ParentCategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.parentCategoryCreate(dto));
    }

    @PostMapping("/create/child")
    public ResponseEntity<ChildCategoryDto> createChildCategory(@RequestBody ChildCategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.childCategoryCreate(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable("id") Long id, @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body("카테고리가 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getParentCategories() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(categoryService.getParentCategories());
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<List<CategoryDto>> getCategoryById(@PathVariable("parentId") Long parentId) {
        log.info("parentId : {}",parentId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(categoryService.getChildCategories(parentId));
    }
}
