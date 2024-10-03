package com.ecommerce.cart.domain.dto;

import jakarta.validation.constraints.Min;
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
public class CartItemUpdateDto {

    @NotNull(message = "장바구니에 담긴 상품 아이디는 필수 입력 값 입니다.")
    private Long id;

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 1, message = "1개 이상 입력해주세요.")
    private int qty;
}
