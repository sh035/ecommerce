package com.ecommerce.product.service;

import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.image.service.ImageService;
import com.ecommerce.product.domain.dto.ProductCreateDto;
import com.ecommerce.product.domain.dto.ProductDetailDto;
import com.ecommerce.product.domain.dto.ProductListResDto;
import com.ecommerce.product.domain.dto.ProductResDto;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import com.ecommerce.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;


    @Transactional(readOnly = true)
    public Slice<ProductListResDto> getProducts(String title, String parentCategory, String childCategory
        ,  String sort, String sortOrder, Pageable pageable) {

        Slice<Product> products = productRepository.searchKeyword(title, parentCategory, childCategory,
             sort, sortOrder, pageable);

        return products.map(ProductListResDto::from);
    }

    @Transactional
    public ProductResDto createProduct(ProductCreateDto dto, List<MultipartFile> files) {
        Category parentCategory = categoryRepository.findById(dto.getParentCategoryId())
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        Category childCategory = categoryRepository.findById(dto.getChildCategoryId())
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        Product product = Product.builder()
            .parentCategory(parentCategory)
            .childCategory(childCategory)
            .title(dto.getTitle())
            .price(dto.getPrice())
            .description(dto.getDescription())
            .deliveryCharge(dto.getDeliveryCharge())
            .qty(dto.getQty())
            .productStatus(ProductStatus.SELL)
            .build();

        List<Image> images = imageService.getImages(files);
        addImages(images, product);
        return ProductResDto.from(productRepository.save(product));
    }

    @Transactional
    public ProductResDto update(Long id, ProductUpdateDto dto,
        List<MultipartFile> files) {
        Product product = getProduct(id);
        Category parentCategory = categoryRepository.findById(dto.getParentCategoryId())
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        Category childCategory = categoryRepository.findById(dto.getChildCategoryId())
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));

        product.update(dto, parentCategory, childCategory);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            dto.getImages().forEach(imageService::deleteFile);
        }

        List<Image> images = imageService.getImages(files);
        addImages(images, product);

        return ProductResDto.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductDetailDto detail(Long id) {
        Product product = getProduct(id);

        return ProductDetailDto.from(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = getProduct(id);

        product.delete(LocalDateTime.now());
        product.getImages().forEach(image -> imageService.deleteFile(image.getImageName()));
        productRepository.save(product);
    }

    private Product getProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return product;
    }

    private void addImages(List<Image> images, Product product) {
        images.stream()
            .map(image -> image.toBuilder()
                .product(product)
                .build())
            .forEach(product::addImages);
    }
}
