package com.sparta.realtomatoapp.order.service;

import com.sparta.realtomatoapp.menu.entity.Menu;
import com.sparta.realtomatoapp.menu.repository.MenuRepository;
import com.sparta.realtomatoapp.order.dto.OrderCreateRequestDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateResponseDto;
import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.order.entity.OrderStatus;
import com.sparta.realtomatoapp.order.repository.OrderRepository;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.store.repository.StoreRepository;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    // = return OrederCreateResponseDto;
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
}
