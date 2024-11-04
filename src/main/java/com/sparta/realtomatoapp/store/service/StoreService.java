package com.sparta.realtomatoapp.store.service;

import com.sparta.realtomatoapp.store.dto.*;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 가게 생성
    @Transactional
    public StoreCreateResponseDTO createStore(StoreCreateRequestDTO requestDto) {
        Store store = Store.builder()
                .storeName(requestDto.getStoreName())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .minPrice(requestDto.getMinPrice())
                .status(requestDto.getStatus())
                .build();

        Store savedStore = storeRepository.save(store);
        return new StoreCreateResponseDTO(savedStore);  // StoreCreateResponseDTO 사용
    }
}