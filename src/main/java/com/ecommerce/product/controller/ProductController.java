package com.ecommerce.product.controller;

import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductListResDto;
import com.ecommerce.product.domain.dto.ProductResDto;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.validator.ValidImage;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<Slice<ProductListResDto>> getProducts(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String parentCategory,
      @RequestParam(required = false) String childCategory,
      @RequestParam(required = false) String sorted,
      @PageableDefault Pageable pageable) {

    return ResponseEntity.ok(productService.getProducts(title, parentCategory, childCategory, sorted, pageable));
  }

  @PostMapping("/create")
  public ResponseEntity<ProductResDto> create(@Valid @RequestPart("dto") ProductCreateDto dto,
      @RequestPart("images") List<@ValidImage MultipartFile> images) {
    return ResponseEntity.ok(productService.createProduct(dto, images));
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<ProductResDto> update(@PathVariable("id") Long id, @Valid @RequestPart("dto") ProductUpdateDto dto,
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
