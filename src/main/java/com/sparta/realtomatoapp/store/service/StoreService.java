package com.sparta.realtomatoapp.store.service;

import com.sparta.realtomatoapp.store.dto.*;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 가게 생성
    @Transactional
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {
        Store store = Store.builder()
                .storeName(requestDto.getStoreName())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .minPrice(requestDto.getMinPrice())
                .status(requestDto.getStatus())
                .build();

        Store savedStore = storeRepository.save(store);
        return new StoreCreateResponseDto(savedStore);  // StoreCreateResponseDto 사용
    }

    // 특정 가게 조회
    public StoreResponseDto getStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        return new StoreResponseDto(store);  // StoreResponseDto 사용
    }

    // 모든 가게 조회
    public StoreListResponseDto getAllStores() {
        List<StoreResponseDto> stores = storeRepository.findAll().stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
        return new StoreListResponseDto(stores);  // StoreListResponseDto 사용
    }

    // 가게 정보 수정
    @Transactional
    public StoreUpdateResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        store.setStoreName(requestDto.getStoreName());
        store.setOpenTime(requestDto.getOpenTime());
        store.setCloseTime(requestDto.getCloseTime());
        store.setMinPrice(requestDto.getMinPrice());

        return new StoreUpdateResponseDto(store);  // StoreUpdateResponseDto 사용
    }

    // 가게 삭제
    @Transactional
    public StoreDeleteResponseDto deleteStore(Long storeId) {
        storeRepository.deleteById(storeId);
        return new StoreDeleteResponseDto(storeId);  // StoreDeleteResponseDto 사용
    }
}