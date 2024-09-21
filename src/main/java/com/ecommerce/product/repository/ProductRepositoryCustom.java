package com.ecommerce.product.repository;

import com.ecommerce.product.domain.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {

    Slice<Product> searchKeyword(String title, String parentCategory, String childCategory,
        String sorted, Pageable pageable);

}
