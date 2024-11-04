package com.sparta.realtomatoapp.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreListResponseDto {
    private final List<StoreResponseDto> stores;

    public StoreListResponseDto(List<StoreResponseDto> stores) {
        this.stores = stores;
    }
}
