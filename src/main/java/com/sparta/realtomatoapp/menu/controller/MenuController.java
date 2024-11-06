package com.sparta.realtomatoapp.menu.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.menu.dto.*;
import com.sparta.realtomatoapp.menu.service.MenuService;
import com.sparta.realtomatoapp.security.Authorized;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/{storeId}")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Authorized(UserRole.STOREOWNER)
    @PostMapping("/menu")
    public ResponseEntity<DataResponseDto<MenuCreateResponseDto>> createMenu(@RequestBody MenuCreateRequestDto requestDto) {
        MenuCreateResponseDto menuData = menuService.createMenu(requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("메뉴 생성 성공", List.of(menuData)));
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<DataResponseDto<MenuNameResponseDto>> getMenu(@PathVariable Long menuId) {
        MenuNameResponseDto menuData = menuService.getMenuById(menuId);
        return ResponseEntity.ok(new DataResponseDto<>("메뉴 단건 조회 성공", List.of(menuData)));
    }

    @GetMapping("/menu")
    public ResponseEntity<DataResponseDto<MenuNameResponseDto>> getAllStores() {
        List<MenuNameResponseDto> MenuDataList = menuService.getAllMenus();
        return ResponseEntity.ok(new DataResponseDto<>("메뉴 다건 조회 성공", MenuDataList));
    }

    @Authorized(UserRole.STOREOWNER)
    @PutMapping("/menu/{menuId}")
    public ResponseEntity<DataResponseDto<MenuUpdateResponseDto>> updateMenu(@PathVariable Long menuId, @RequestBody MenuUpdateRequestDto requestDto) {
        MenuUpdateResponseDto menuData = menuService.updateMenu(menuId, requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("메뉴 정보 수정 성공", List.of(menuData)));
    }

    @Authorized(UserRole.STOREOWNER)
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<BaseResponseDto> deleteMenu(@PathVariable Long menuId) {
        MenuDeleteResponseDto menuData = menuService.deleteMenu(menuId);
        return ResponseEntity.ok(BaseResponseDto.baseResponseBuilder().message(menuData.getMessage()).build());
    }

}
