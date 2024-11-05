package com.sparta.realtomatoapp.review.service;

import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewDeleteResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {


    public ReviewCreateResponseDto createReview(ReviewCreateRequestDto requestDto) {
        return null;
    }

    public List<ReviewListResponseDto> getAllReviews() {
        return null;

    }

    public ReviewDeleteResponseDto deleteReview(Long reviewId) {
        return null;
    }
}
