package com.ecommerce.order.domain.dto;

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

    private Long productId;
    private String title;
    private int price;
    private int qty;
    private OrderStatus status;
    private String thumbnailUrl;

    public static OrderItemDto from(OrderItem orderItem) {
        return OrderItemDto.builder()
            .productId(orderItem.getProduct().getId())
            .title(orderItem.getProduct().getTitle())
            .price(orderItem.getPrice())
            .qty(orderItem.getQty())
            .status(orderItem.getStatus())
            .build();
    }
}
