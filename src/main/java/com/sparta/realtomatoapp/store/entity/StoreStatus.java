package com.sparta.realtomatoapp.store.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreStatus {
    OPEN("운영 중"),
    CLOSED("휴점 중"),
    PERMANENTLY_CLOSED("폐점");

    private final String description;
}
