package com.ecommerce.product.domain.dto;

import com.ecommerce.product.domain.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateDto {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    @Size(max = 100, message = "상품 이름은 100자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "가격은 음수를 입력할 수 없습니다.")
    private int price;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    @Size(max = 3000, message = "상품 설명은 3000자까지 입력할 수 있습니다.")
    private String description;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private long parentCategoryId;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private long childCategoryId;

    @NotBlank(message = "배송비를 입력해주세요.")
    @PositiveOrZero(message = "0 이상 입력해주세요.")
    private int deliveryCharge;

    @NotBlank(message = "수량을 입력해주세요.")
    @Size(min = 1, message = "1개 이상 입력해주세요.")
    private int qty;

    private ProductStatus productStatus;

    private List<String> images;


}
