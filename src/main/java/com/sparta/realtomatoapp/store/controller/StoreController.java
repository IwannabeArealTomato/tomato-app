package com.sparta.realtomatoapp.store.controller;

import com.sparta.realtomatoapp.store.dto.*;
import com.sparta.realtomatoapp.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreCreateResponseDto> createStore(@RequestBody StoreCreateRequestDto requestDto) {
        StoreCreateResponseDto response = storeService.createStore(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        StoreResponseDto response = storeService.getStoreById(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<StoreListResponseDto> getAllStores() {
        StoreListResponseDto response = storeService.getAllStores();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreUpdateResponseDto> updateStore(@PathVariable Long storeId, @RequestBody StoreUpdateRequestDto requestDto) {
        StoreUpdateResponseDto response = storeService.updateStore(storeId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreDeleteResponseDto> deleteStore(@PathVariable Long storeId) {
        StoreDeleteResponseDto response = storeService.deleteStore(storeId);
        return ResponseEntity.ok(response);
    }
}