package com.sparta.realtomatoapp.store.dto;

import com.sparta.realtomatoapp.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreCreateResponseDto {
    private Long storeId;
    private String storeName;
    private String openTime;
    private String closeTime;
    private Long minPrice;
    private String status;

    public StoreCreateResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime().toString();
        this.closeTime = store.getCloseTime().toString();
        this.minPrice = store.getMinPrice();
        this.status = store.getStatus().toString();
    }
}
