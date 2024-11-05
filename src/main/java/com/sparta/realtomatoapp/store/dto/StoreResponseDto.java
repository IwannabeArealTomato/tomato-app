package com.sparta.realtomatoapp.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long minPrice;
}