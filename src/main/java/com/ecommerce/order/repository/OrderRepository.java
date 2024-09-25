package com.ecommerce.order.repository;

import com.ecommerce.order.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.member.email = :email order by o.createdAt desc")
    Page<Order> findOrdersByEmail(String email, Pageable pageable);

}
