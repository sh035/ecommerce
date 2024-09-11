package com.ecommerce.category.controller;

import com.ecommerce.category.domain.dto.CategoryDto;
import com.ecommerce.category.domain.dto.CategoryUpdateDto;
import com.ecommerce.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody CategoryDto dto) {
    return ResponseEntity.ok(categoryService.create(dto));
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CategoryUpdateDto dto) {
    return ResponseEntity.ok(categoryService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {

    return ResponseEntity.ok(categoryService.delete(id));
  }

  @GetMapping
  public ResponseEntity<?> getAllCategories() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(categoryService.findById(id));
  }
}
