package com.sparta.realtomatoapp.store.dto;

import com.sparta.realtomatoapp.store.entity.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateResponseDto {
    private Long storeId;
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long minPrice;
    private StoreStatus status;
}
