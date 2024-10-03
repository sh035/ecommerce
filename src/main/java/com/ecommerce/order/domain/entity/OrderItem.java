package com.ecommerce.order.domain.entity;

import com.ecommerce.global.entity.BaseTime;
import com.ecommerce.order.domain.enums.OrderStatus;
import com.ecommerce.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int qty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public static OrderItem createOrderItem(Product product, int qty) {
        product.removeQty(qty);

        return OrderItem.builder()
            .product(product)
            .price(product.getPrice())
            .qty(qty)
            .status(OrderStatus.ORDER)
            .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        this.status = OrderStatus.CANCEL;
        this.getProduct().addQty(qty);
    }
}
