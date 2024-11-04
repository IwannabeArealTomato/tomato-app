package com.sparta.realtomatoapp.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreListResponseDTO {
    private final List<StoreResponseDTO> stores;

    public StoreListResponseDTO(List<StoreResponseDTO> stores) {
        this.stores = stores;
    }
}
