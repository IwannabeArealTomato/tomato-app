package com.sparta.realtomatoapp.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequestDto {
    private String menuName;
    private Integer price;
}
