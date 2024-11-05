package com.sparta.realtomatoapp.store.service;

import com.sparta.realtomatoapp.store.dto.*;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.store.repository.StoreRepository;
import com.sparta.realtomatoapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {
        Store store = Store.builder()
                .user(User.builder().userId(1L).build())
                .storeName(requestDto.getStoreName())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .minPrice(requestDto.getMinPrice())
                .status(requestDto.getStatus())
                .build();
        Store savedStore = storeRepository.save(store);

        return new StoreCreateResponseDto(
                savedStore.getStoreId(),
                savedStore.getStoreName(),
                savedStore.getOpenTime(),
                savedStore.getCloseTime(),
                savedStore.getMinPrice(),
                savedStore.getStatus()
        );
    }

    public StoreResponseDto getStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));

        return new StoreResponseDto(
                store.getStoreId(),
                store.getStoreName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinPrice()
        );
    }

    public List<StoreListResponseDto> getAllStores() {
        return storeRepository.findAll().stream()
                .map(store -> new StoreListResponseDto(
                        store.getStoreId(),
                        store.getStoreName(),
                        store.getOpenTime(),
                        store.getCloseTime(),
                        store.getMinPrice(),
                        store.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreUpdateResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));

        store.setStoreName(requestDto.getStoreName());
        store.setOpenTime(requestDto.getOpenTime());
        store.setCloseTime(requestDto.getCloseTime());
        store.setMinPrice(requestDto.getMinPrice());

        return new StoreUpdateResponseDto("Update successful", store.getStoreName(), store.getMinPrice());
    }

    @Transactional
    public StoreDeleteResponseDto deleteStore(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("Store not found with ID: " + storeId);
        }
        storeRepository.deleteById(storeId);
        return new StoreDeleteResponseDto("Delete successful");
    }
}
