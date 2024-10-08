package com.ecommerce.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    SELL("판매중"), SOLD_OUT("품절"), STOP_SELL("판매 종료");

    private String value;
}
