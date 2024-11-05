package com.sparta.realtomatoapp.review.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewDeleteResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import com.sparta.realtomatoapp.review.service.ReviewService;
import com.sparta.realtomatoapp.security.Authorized;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // TODO : 리뷰 CRUD
    //리뷰 생성
    @Authorized(UserRole.ADMIN)
    @PostMapping("/order/{orderId}/review")
    public ResponseEntity<DataResponseDto<ReviewCreateResponseDto>> creatReview(
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewCreateResponseDto reviewData = reviewService.createReview(orderId,requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("리뷰 생성 성공", List.of(reviewData)));
    }

    @GetMapping("/store/{storeId}/reviews")
    //리뷰 전체 조회
    public ResponseEntity<DataResponseDto<ReviewListResponseDto>> getAllReviews() {
        List<ReviewListResponseDto> allReviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new DataResponseDto<>("리뷰 다건 조회 성공", List.of()));
    }

    @DeleteMapping("/order/{orderId}/review/{reviewId}")
    //리뷰 삭제
    public ResponseEntity<BaseResponseDto> deleteReview(Long reviewId) {
        ReviewDeleteResponseDto reviewData = reviewService.deleteReview(reviewId);
        return null;
    }
}
