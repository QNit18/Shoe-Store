package com.shoestore.repositories;

import com.shoestore.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find order of user
    List<Order> findByUserId(Long userId);
}
