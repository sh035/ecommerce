package com.ecommerce.cart.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItemDeleteDto {

    @NotNull(message = "장바구니에 담긴 상품 아이디는 필수 입력 값 입니다.")
    private Long id;
}
