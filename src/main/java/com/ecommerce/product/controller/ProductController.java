package com.ecommerce.product.controller;

import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.service.ProductService;
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
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody ProductCreateDto dto) {
    return ResponseEntity.ok(productService.createProduct(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProductUpdateDto dto) {
    return ResponseEntity.ok(productService.updateProduct(id, dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> details(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productService.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productService.delete(id));
  }
}
