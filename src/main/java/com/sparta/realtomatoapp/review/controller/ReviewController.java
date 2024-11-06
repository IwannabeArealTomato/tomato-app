package com.sparta.realtomatoapp.review.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import com.sparta.realtomatoapp.review.service.ReviewService;
import com.sparta.realtomatoapp.security.Authorized;
import com.sparta.realtomatoapp.user.dto.AuthUser;
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
    @Authorized(UserRole.USER)
    @PostMapping("/order/{orderId}/review")
    public ResponseEntity<DataResponseDto<ReviewCreateResponseDto>> creatReview(
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody ReviewCreateRequestDto requestDto,
            @LoginUser AuthUser authUser
    ) {
        ReviewCreateResponseDto reviewData = reviewService.createReview(orderId,requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("리뷰 생성 성공", List.of(reviewData)));
    }

    //사용자- 특정가게 리뷰 전체 조회
    @Authorized(UserRole.USER)
    @GetMapping("/store/{storeId}/reviews")
    public ResponseEntity<DataResponseDto<ReviewListResponseDto>> getAllReviews(@LoginUser AuthUser authUser,@PathVariable(name = "storeId") Long storeId) {
        List<ReviewListResponseDto> allReviews = reviewService.getAllReviews(authUser, storeId);
        return ResponseEntity.ok(new DataResponseDto<>("리뷰 다건 조회 성공", allReviews));
    }

    //리뷰 삭제
    @Authorized(UserRole.USER)
    @DeleteMapping("/order/{orderId}/review/{reviewId}")
    public ResponseEntity<BaseResponseDto> deleteReview(
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }
}
