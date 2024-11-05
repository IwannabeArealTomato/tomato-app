package com.sparta.realtomatoapp.review.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateRequestDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateResponseDto;
import com.sparta.realtomatoapp.review.entity.Review;
import com.sparta.realtomatoapp.store.dto.StoreListResponseDto;
import org.springframework.http.ResponseEntity;

public class ReviewController {

    // TODO : 리뷰 CRUD
    //리뷰 생성
    public ResponseEntity<DataResponseDto<ReviewCreateResponseDto>> creatReview(ReviewCreateRequestDto requestDto) {
        return null;
    }

    //리뷰 전체 조회
    public ResponseEntity<DataResponseDto<ReviewListResponseDto>> getAllReviews() {
        return null;
    }

    //리뷰 삭제
    public ResponseEntity<BaseResponseDto> deleteReview(Long reviewId) {
        return null;
    }
}
