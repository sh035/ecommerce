package com.ecommerce.cart.domain.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItemsDeleteDto {

    @Size(min = 1, message = "삭제할 상품이 최소한 하나 이상 필요합니다.")
    private List<Long> ids;
}
