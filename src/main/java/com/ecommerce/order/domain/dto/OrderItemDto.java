package com.ecommerce.order.domain.dto;

import com.ecommerce.image.domain.dto.ImageDto;
import com.ecommerce.image.domain.enums.ImageType;
import com.ecommerce.order.domain.entity.OrderItem;
import com.ecommerce.order.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;
    private Long productId;
    private String title;
    private int price;
    private int qty;
    private OrderStatus status;
    private ImageDto thumbnailUrl;

    public static OrderItemDto from(OrderItem orderItem) {
        return OrderItemDto.builder()
            .id(orderItem.getId())
            .productId(orderItem.getProduct().getId())
            .title(orderItem.getProduct().getTitle())
            .price(orderItem.getPrice())
            .qty(orderItem.getQty())
            .status(orderItem.getStatus())
            .thumbnailUrl(orderItem.getProduct().getImages().stream()
                .filter(image -> image.getImageType() == ImageType.THUMBNAIL)
                .findFirst()
                .map(ImageDto::from)
                .orElse(null))
            .build();
    }
}
