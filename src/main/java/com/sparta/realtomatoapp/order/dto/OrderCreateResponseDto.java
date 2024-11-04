package com.sparta.realtomatoapp.order.dto;

import com.sparta.realtomatoapp.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    //{"message":, "data": [{ "storeId": 가게 고유 번호, "menuId": 메뉴 고유 번호, "amount": 개수, "status": "PENDING" }] }
    private Long storeId;
    private Long menuId;
    private int amount;
    private OrderStatus status;
}

