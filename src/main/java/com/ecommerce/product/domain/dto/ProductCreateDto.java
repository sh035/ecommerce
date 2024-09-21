package com.ecommerce.product.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductCreateDto {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    @Size(max = 100, message = "상품 이름은 100자까지 입력할 수 있습니다.")
    private String name;

    @PositiveOrZero(message = "올바른 값을 입력해주세요.")
    private int price;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    @Size(max = 3000, message = "상품 설명은 3000자까지 입력할 수 있습니다.")
    private String description;

    @NotNull(message = "카테고리를 선택해주세요.")
    private long parentCategoryId;

    @NotNull(message = "카테고리를 선택해주세요.")
    private long childCategoryId;

    @NotNull(message = "배송비를 입력해주세요.")
    @PositiveOrZero(message = "올바른 값을 입력해주세요.")
    private int deliveryCharge;

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 1, message = "1개 이상 입력해주세요.")
    private int qty;
}
