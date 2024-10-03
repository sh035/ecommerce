package com.ecommerce.order.controller;

import com.ecommerce.global.security.auth.PrincipalDetails;
import com.ecommerce.order.domain.dto.OrderCartItemDto;
import com.ecommerce.order.domain.dto.OrderDto;
import com.ecommerce.order.domain.dto.OrderItemCancelDto;
import com.ecommerce.order.domain.dto.OrderListResDto;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity<Page<OrderListResDto>> getOrders(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrders(principalDetails.getUsername(), pageable));
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody OrderDto dto) {

        orderService.order(principalDetails.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("구매가 완료되었습니다.");
    }

    @PostMapping("/cart")
    public ResponseEntity<String> createOrderCartItems(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody OrderCartItemDto dto) {

        orderService.cartOrders(principalDetails.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("구매가 완료되었습니다.");
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody OrderItemCancelDto dto) {

        orderService.cancel(principalDetails.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

}
