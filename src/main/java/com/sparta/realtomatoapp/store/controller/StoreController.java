package com.sparta.realtomatoapp.store.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.store.dto.*;
import com.sparta.realtomatoapp.store.service.StoreService;
import com.sparta.realtomatoapp.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<DataResponseDto<StoreCreateResponseDto>> createStore(@LoginUser User user, @Valid @RequestBody StoreCreateRequestDto requestDto) {
        StoreCreateResponseDto storeData = storeService.createStore(user, requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("가게 생성 성공", List.of(storeData)));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<DataResponseDto<StoreResponseDto>> getStore(@PathVariable Long storeId) {
        StoreResponseDto storeData = storeService.getStoreById(storeId);
        return ResponseEntity.ok(new DataResponseDto<>("가게 단건 조회 성공", List.of(storeData)));
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<StoreListResponseDto>> getAllStores() {
        List<StoreListResponseDto> storeDataList = storeService.getAllStores();
        return ResponseEntity.ok(new DataResponseDto<>("가게 다건 조회 성공", storeDataList));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<DataResponseDto<StoreUpdateResponseDto>> updateStore(@LoginUser User user, @PathVariable Long storeId, @RequestBody StoreUpdateRequestDto requestDto) {
        StoreUpdateResponseDto storeData = storeService.updateStore(user, storeId, requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("가게 정보 수정 성공", List.of(storeData)));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<BaseResponseDto> deleteStore(@LoginUser User user, @PathVariable Long storeId) {
        StoreDeleteResponseDto storeData = storeService.deleteStore(user, storeId);
        return ResponseEntity.ok(BaseResponseDto.baseResponseBuilder().message(storeData.getMessage()).build());
    }
}
