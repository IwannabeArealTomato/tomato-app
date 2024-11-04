package com.sparta.realtomatoapp.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreDeleteResponseDto {
    private String message;
    private Long deletedStoreId;

    public StoreDeleteResponseDto(Long storeId) {
        this.message = "가게 삭제 성공";
        this.deletedStoreId = storeId;
    }
}
