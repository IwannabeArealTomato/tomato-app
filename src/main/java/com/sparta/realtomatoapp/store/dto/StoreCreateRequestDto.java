package com.sparta.realtomatoapp.store.dto;

import com.sparta.realtomatoapp.store.entity.StoreStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequestDto {
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long minPrice;
    private StoreStatus status;
}
