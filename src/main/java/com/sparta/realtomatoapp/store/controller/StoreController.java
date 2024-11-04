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
    public ResponseEntity<StoreCreateResponseDTO> createStore(@RequestBody StoreCreateRequestDTO requestDto) {
        StoreCreateResponseDTO response = storeService.createStore(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDTO> getStore(@PathVariable Long storeId) {
        StoreResponseDTO response = storeService.getStoreById(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<StoreListResponseDTO> getAllStores() {
        StoreListResponseDTO response = storeService.getAllStores();
        return ResponseEntity.ok(response);
    }
}