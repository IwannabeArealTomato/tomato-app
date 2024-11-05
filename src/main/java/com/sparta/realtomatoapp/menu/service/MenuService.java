package com.sparta.realtomatoapp.menu.service;

import com.sparta.realtomatoapp.menu.dto.*;
import com.sparta.realtomatoapp.menu.entity.Menu;
import com.sparta.realtomatoapp.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuCreateResponseDto createMenu(MenuCreateRequestDto requestDto) {
        Menu menu = Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .build();
        Menu savedMenu = menuRepository.save(menu);

        return new MenuCreateResponseDto(
                savedMenu.getMenuId(),
                savedMenu.getMenuName(),
                savedMenu.getPrice()
        );
    }

    public MenuNameResponseDto getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("menu not found with ID: " + menuId));

        return new MenuNameResponseDto(
                menu.getMenuName(),
                menu.getPrice()
        );
    }

    public List<MenuNameResponseDto> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(menu -> new MenuNameResponseDto(
                        menu.getMenuName(),
                        menu.getPrice()
                ))
                .toList();
    }

    @Transactional
    public MenuUpdateResponseDto updateMenu(Long menuId, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found with ID: " + menuId));

       menu.updateMenu(requestDto.getMenuName(), requestDto.getPrice());

        return new MenuUpdateResponseDto(menu.getMenuName(), menu.getPrice());
    }

    @Transactional
    public MenuDeleteResponseDto deleteMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("Menu not found with ID: " + menuId);
        }
        menuRepository.deleteById(menuId);
        return new MenuDeleteResponseDto("메뉴 삭제 완료");
    }

}
