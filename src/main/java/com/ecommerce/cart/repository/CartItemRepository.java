package com.ecommerce.cart.repository;

import com.ecommerce.cart.domain.entity.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);
}
