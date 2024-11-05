package com.sparta.realtomatoapp.order.dto;

import com.sparta.realtomatoapp.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long storeId;
    private Long userId;
    private Long menuId;
    private int amount;
    private OrderStatus status;
}