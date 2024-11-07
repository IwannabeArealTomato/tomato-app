package com.sparta.realtomatoapp.order.repository;

import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByStore(Store store);

    List<Order> findAllByStoreAndUser(Store store, User user);
}
