package com.sparta.realtomatoapp.order.dto;

import com.sparta.realtomatoapp.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    private Long storeId;
    private Long menuId;
    private int amount;
    private OrderStatus status;
}

