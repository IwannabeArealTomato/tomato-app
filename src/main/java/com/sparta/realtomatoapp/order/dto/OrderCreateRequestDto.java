package com.sparta.realtomatoapp.order.dto;

import lombok.Getter;

@Getter
public class OrderCreateRequestDto {
    private int amount;
    private Long userId;
    private Long storeId;
    private Long menuId;
}
