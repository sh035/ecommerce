package com.ecommerce.order.service;

import com.ecommerce.cart.domain.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.repository.MemberRepository;
import com.ecommerce.order.domain.dto.OrderCartItemDto;
import com.ecommerce.order.domain.dto.OrderDto;
import com.ecommerce.order.domain.dto.OrderItemCancelDto;
import com.ecommerce.order.domain.dto.OrderItemDto;
import com.ecommerce.order.domain.dto.OrderListResDto;
import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.domain.entity.OrderItem;
import com.ecommerce.order.exception.NotEnoughPointException;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public Page<OrderListResDto> getOrders(String email, Pageable pageable) {

        Page<Order> orders = orderRepository.findOrdersByEmail(email, pageable);

        if (orders.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Page<OrderListResDto> dto = orders.map(order -> {
            List<OrderItemDto> items = order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .toList();

            return OrderListResDto.builder()
                .orderId(order.getId())
                .orderItems(items)
                .createdAt(order.getCreatedAt())
                .build();
        });

        return dto;
    }

    //Todo: details 추가해야됨

    @Transactional
    public void order(String email, OrderDto dto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        int totalPrice = product.getPrice() * dto.getQty() + product.getDeliveryCharge();

        validateMemberPoint(totalPrice, member);

        OrderItem orderItem = OrderItem.createOrderItem(product, dto.getQty());

        Order order = Order.builder()
            .member(member)
            .build();
        order.addOrderItem(orderItem);

        member.charge(totalPrice);

        orderRepository.save(order);
    }

    @Transactional
    public void cartOrders(String email, OrderCartItemDto dto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        List<CartItem> cartItems = cartItemRepository.findAllById(dto.getOrderIds());

        int totalPrice = cartItems.stream().mapToInt(CartItem::getPrice).sum();

        validateMemberPoint(totalPrice, member);

        Order order = Order.builder()
            .member(member)
            .build();

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = OrderItem.createOrderItem(cartItem.getProduct(), cartItem.getQty());
            order.addOrderItem(orderItem);
        });

        member.charge(totalPrice);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);
    }

    public void cancel(String email, OrderItemCancelDto dto) {

        OrderItem orderItem = orderItemRepository.findByIdAndEmail(dto.getOrderItemId(), email)
            .orElseThrow(() -> new NoSuchElementException("주문상품이 존재하지 않습니다."));

        orderItem.cancel();

        orderItemRepository.save(orderItem);
    }


    private void validateMemberPoint(int totalPrice, Member member) {
        if (totalPrice > member.getPoint()) {
            throw new NotEnoughPointException();
        }
    }
}
