package com.sparta.realtomatoapp.store.dto;

import com.sparta.realtomatoapp.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateResponseDTO {
    private Long storeId;
    private String storeName;
    private String openTime;
    private String closeTime;
    private Long minPrice;
    private String status;

    public StoreUpdateResponseDTO(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime().toString();
        this.closeTime = store.getCloseTime().toString();
        this.minPrice = store.getMinPrice();
        this.status = store.getStatus().toString();
    }
}
