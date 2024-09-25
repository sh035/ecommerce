package com.ecommerce.order.controller;

import com.ecommerce.global.security.auth.PrincipalDetails;
import com.ecommerce.order.domain.dto.OrderDto;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody OrderDto dto) {

        orderService.order(principalDetails.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("구매가 완료되었습니다.");
    }
}
