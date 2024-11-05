package com.sparta.realtomatoapp.order.dto;

import com.sparta.realtomatoapp.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderUpdateRequestDto {
    private Long orderId;
    private OrderStatus status;
}
