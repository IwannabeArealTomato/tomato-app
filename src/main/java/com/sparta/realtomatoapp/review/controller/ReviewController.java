package com.sparta.realtomatoapp.review.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import com.sparta.realtomatoapp.review.service.ReviewService;
import com.sparta.realtomatoapp.store.service.StoreService;
import org.springframework.http.ResponseEntity;

public class ReviewController {

    private final ReviewService reviewService;

    // TODO : 리뷰 CRUD
    //리뷰 생성
    public ResponseEntity<DataResponseDto<ReviewCreateResponseDto>> creatReview(ReviewCreateRequestDto requestDto) {
        reviewService.createReview(requestDto);
        return null;
    }

    //리뷰 전체 조회
    public ResponseEntity<DataResponseDto<ReviewListResponseDto>> getAllReviews() {
        reviewService.getAllReviews();
        return null;
    }

    //리뷰 삭제
    public ResponseEntity<BaseResponseDto> deleteReview(Long reviewId) {
        reviewService.deleteReview(reviewId)
        return null;
    }
}
