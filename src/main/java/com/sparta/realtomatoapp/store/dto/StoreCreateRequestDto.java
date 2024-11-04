package com.sparta.realtomatoapp.store.dto;

import com.sparta.realtomatoapp.store.entity.StoreStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StoreCreateRequestDto {

    @NotNull(message = "가게 이름은 필수 입니다.")
    @Size(min = 3, max = 20, message = "가게 이름은 3글자 이상 20글자 이하여야 합니다.")
    private String storeName;

    @NotNull(message = "개장 시간을 필수로 입력하셔야 합니다.")
    private LocalTime openTime;

    @NotNull(message = "마감 시간을 필수로 입력하셔야 합니다.")
    private LocalTime closeTime;

    @NotNull(message = "최소 주문 금액을 필수로 입력하셔야 합니다.")
    @Min(value = 15000, message = "최소 주문 금액은 15000원 이상이어야 합니다.")
    private Long minPrice;

    //가게 상태
    @NotNull(message = "가게 상태를 입력해 주세요.")
    private StoreStatus status;
}
