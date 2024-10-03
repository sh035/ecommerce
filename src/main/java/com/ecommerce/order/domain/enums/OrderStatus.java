package com.ecommerce.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("구매완료"), CANCEL("취소");

    private String value;
}
