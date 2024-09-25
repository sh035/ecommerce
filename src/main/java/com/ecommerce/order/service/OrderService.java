package com.ecommerce.order.service;

import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.repository.MemberRepository;
import com.ecommerce.order.domain.dto.OrderDto;
import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.domain.entity.OrderItem;
import com.ecommerce.order.domain.enums.OrderStatus;
import com.ecommerce.order.exception.NotEnoughPointException;
import com.ecommerce.order.exception.OutOfQtyException;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void order(String email, OrderDto dto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        if (product.getPrice() > member.getPoint()) {
            throw new NotEnoughPointException();
        }

        if (dto.getQty() > product.getQty()) {
            throw new OutOfQtyException();
        }

        int totalPrice = product.getPrice() * dto.getQty() + product.getDeliveryCharge();

        OrderItem orderItem = OrderItem.builder()
            .product(product)
            .price(totalPrice)
            .qty(dto.getQty())
            .build();

        Order order = Order.builder()
            .member(member)
            .status(OrderStatus.ORDER)
            .build();

        order.addOrderItem(orderItem);
        member.charge(totalPrice);

        orderRepository.save(order);
    }
}
