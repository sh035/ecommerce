package com.ecommerce.cart.controller;

import com.ecommerce.cart.domain.dto.CartItemAddDto;
import com.ecommerce.cart.domain.dto.CartItemDeleteDto;
import com.ecommerce.cart.domain.dto.CartItemDetailDto;
import com.ecommerce.cart.domain.dto.CartItemUpdateDto;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.global.security.auth.PrincipalDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDetailDto>> getCartItemsDetail(
        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(cartService.cartDetails(principalDetails.getUsername()));
    }


    @PostMapping("/add")
    public ResponseEntity<String> addCartItem(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody CartItemAddDto dto) {

        cartService.addCartItem(principalDetails.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("장바구니에 상품이 담겼습니다.");
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateCartItem(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody CartItemUpdateDto dto) {

        cartService.updateCartItem(principalDetails.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCartItem(@AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody CartItemDeleteDto dto) {
        cartService.deleteCartItem(principalDetails.getUsername(), dto);
        return ResponseEntity.ok().build();
    }
}
