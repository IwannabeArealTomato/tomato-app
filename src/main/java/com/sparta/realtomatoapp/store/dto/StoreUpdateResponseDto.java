package com.sparta.realtomatoapp.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateResponseDto {
    private String message;
    private String menuName;
    private long price;
}
