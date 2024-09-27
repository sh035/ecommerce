package com.ecommerce.order.repository;

import com.ecommerce.order.domain.entity.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :id AND oi.order.member.email = :email")
    Optional<OrderItem> findByIdAndEmail(Long id, String email);
}
