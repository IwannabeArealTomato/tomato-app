package com.sparta.realtomatoapp.order.repository;

import com.sparta.realtomatoapp.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
