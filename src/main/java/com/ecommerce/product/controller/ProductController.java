package com.ecommerce.product.controller;

import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestPart ProductCreateDto dto,
      @RequestPart List<MultipartFile> images) {
    return ResponseEntity.ok(productService.createProduct(dto, images));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestPart ProductUpdateDto dto,
      @RequestPart List<MultipartFile> images) {
    return ResponseEntity.ok(productService.updateProduct(id, dto, images));
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
