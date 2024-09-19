package com.ecommerce.product.controller;

import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductResponseDto;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.validator.ValidImage;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<ProductResponseDto> create(@Valid @RequestPart("dto") ProductCreateDto dto,
      @RequestPart("images") List<@ValidImage MultipartFile> images) {
    return ResponseEntity.ok(productService.createProduct(dto, images));
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<ProductResponseDto> update(@PathVariable("id") Long id, @Valid @RequestPart("dto") ProductUpdateDto dto,
      @RequestPart("images") List<@ValidImage MultipartFile> images) {
    return ResponseEntity.ok(productService.update(id, dto, images));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> details(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productService.detail(id));
  }

  @PostMapping("/delete/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    productService.delete(id);
    return ResponseEntity.ok().build();
  }
}
