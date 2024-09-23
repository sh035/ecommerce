package com.ecommerce.cart.domain.dto;

import com.ecommerce.cart.domain.entity.CartItem;
import com.ecommerce.image.domain.dto.ImageDto;
import com.ecommerce.image.domain.enums.ImageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItemDetailDto {

    private Long id;
    private String title;
    private int price;
    private int totalPrice;
    private int totalQty;
    private int deliveryCharge;
    private ImageDto thumbnailUrl;

    public static CartItemDetailDto from(CartItem cartItem) {
        return CartItemDetailDto.builder()
            .id(cartItem.getId())
            .title(cartItem.getProduct().getTitle())
            .price(cartItem.getProduct().getPrice())
            .totalPrice(cartItem.getPrice())
            .totalQty(cartItem.getQty())
            .deliveryCharge(cartItem.getProduct().getDeliveryCharge())
            .thumbnailUrl(cartItem.getProduct().getImages().stream()
                .filter(image -> image.getImageType() == ImageType.THUMBNAIL)
                .findFirst()
                .map(ImageDto::from)
                .orElse(null))
            .build();
    }
}
