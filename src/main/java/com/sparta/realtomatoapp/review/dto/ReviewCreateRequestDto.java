package com.sparta.realtomatoapp.review.dto;

import com.sparta.realtomatoapp.review.entity.Review;

public class ReviewCreateRequestDto {

    private String comment;
    private int rating;

    public Review toEntity() {
        return Review.builder()
                .comment(comment)
                .rating(rating)
                .build();
    }
}
