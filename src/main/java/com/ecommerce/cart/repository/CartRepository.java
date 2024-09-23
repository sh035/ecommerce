package com.ecommerce.cart.repository;

import com.ecommerce.cart.domain.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberEmail(String email);
}
