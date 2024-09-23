package com.ecommerce.cart.service;

import com.ecommerce.cart.domain.dto.CartItemAddDto;
import com.ecommerce.cart.domain.dto.CartItemDeleteDto;
import com.ecommerce.cart.domain.dto.CartItemDetailDto;
import com.ecommerce.cart.domain.dto.CartItemUpdateDto;
import com.ecommerce.cart.domain.entity.Cart;
import com.ecommerce.cart.domain.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.repository.MemberRepository;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemDetailDto> cartDetails(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMemberEmail(member.getEmail())
            .orElseGet(() -> Cart.builder()
                .member(member)
                .cartItems(new ArrayList<>()) // 빈 카트 아이템 리스트 초기화
                .build());

        return cart.getCartItems().stream()
            .map(CartItemDetailDto::from)
            .collect(Collectors.toList());

    }

    @Transactional
    public void addCartItem(String email, CartItemAddDto dto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMemberEmail(member.getEmail())
            .orElseGet(() -> Cart.builder()
                .member(member)
                .cartItems(new ArrayList<>())
                .build());

        CartItem cartItem = CartItem.builder()
            .cart(cart)
            .product(product)
            .qty(dto.getQty())
            .price(product.getPrice() * dto.getQty())
            .build();

        cart.addCartItem(cartItem);

        cartRepository.save(cart);
    }

    public void updateCartItem(String email, CartItemUpdateDto dto) {
        Cart cart = cartRepository.findByMemberEmail(email)
            .orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다."));

        CartItem cartItem = cartItemRepository.findByIdAndCartId(dto.getId(), cart.getId())
            .orElseThrow(() -> new NoSuchElementException("장바구니에 해당 상품이 존재하지 않습니다."));

        cartItem.update(cartItem.getProduct().getPrice() * dto.getQty() , dto.getQty());

        cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(String email, CartItemDeleteDto dto) {
        Cart cart = cartRepository.findByMemberEmail(email)
            .orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다."));

        CartItem cartItem = cartItemRepository.findByIdAndCartId(dto.getId(), cart.getId())
            .orElseThrow(() -> new NoSuchElementException("장바구니에 해당 상품이 존재하지 않습니다."));

        cartItem.delete();

        cartItemRepository.save(cartItem);
    }

    // TODO: 장바구니에 선택된 상품들 삭제 구현해야함

    // TODO: 장바구니에 담긴 목록 구매 구현해야함
}
