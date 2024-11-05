package com.sparta.realtomatoapp.review.service;

import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewDeleteResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import com.sparta.realtomatoapp.review.entity.Review;
import com.sparta.realtomatoapp.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewCreateResponseDto createReview(ReviewCreateRequestDto requestDto) {
      Review review = requestDto.toEntity();
        Review savedReview = reviewRepository.save(review);
        return savedReview.toResponseDto();
    }

    public List<ReviewListResponseDto> getAllReviews() {
        return null;

    }

    public ReviewDeleteResponseDto deleteReview(Long reviewId) {
        return null;
    }
}
