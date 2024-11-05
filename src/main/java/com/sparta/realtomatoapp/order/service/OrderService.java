package com.sparta.realtomatoapp.order.service;

import com.sparta.realtomatoapp.menu.entity.Menu;
import com.sparta.realtomatoapp.menu.repository.MenuRepository;
import com.sparta.realtomatoapp.order.dto.OrderCreateRequestDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateResponseDto;
import com.sparta.realtomatoapp.order.dto.OrderUpdateRequestDto;
import com.sparta.realtomatoapp.order.dto.OrderUpdateResponseDto;
import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.order.entity.OrderStatus;
import com.sparta.realtomatoapp.order.repository.OrderRepository;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.store.repository.StoreRepository;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(()->new IllegalArgumentException("Menu not found"));

        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(()->new IllegalArgumentException("Store not found"));

        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .amount(requestDto.getAmount())
                .user(user)
                .menu(menu)
                .store(store)
                .build();

        orderRepository.save(order);

        return OrderCreateResponseDto.builder()
                .storeId(store.getStoreId())
                .menuId(menu.getMenuId())
                .amount(order.getAmount())
                .status(order.getStatus())
                .build();
    }

    @Transactional
    public OrderUpdateResponseDto updateOrder(Long orderId, OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("Order not found"));

        order.updateOrder(requestDto.getStatus());

        return OrderUpdateResponseDto.builder()
                .storeId(order.getStore().getStoreId())
                .menuId(order.getMenu().getMenuId())
                .amount(order.getAmount())
                .status(order.getStatus())
                .build();
    }
}
