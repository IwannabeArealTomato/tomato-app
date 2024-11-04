package com.sparta.realtomatoapp.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreUpdateRequestDTO {
    @NotNull(message = "가게 이름을 필수로 입력하셔야 합니다.")
    private String storeName;
    @NotNull(message = "개장 시간을 필수로 입력하셔야 합니다.")
    private LocalTime openTime;
    @NotNull(message = "마감 시간을 필수로 입력하셔야 합니다.")
    private LocalTime closeTime;
    @NotNull(message = "최소 금액을 필수로 입력하셔야 합니다.")
    private Long minPrice;
}
