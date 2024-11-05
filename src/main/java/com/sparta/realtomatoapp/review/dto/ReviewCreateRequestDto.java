package com.sparta.realtomatoapp.review.dto;

import com.sparta.realtomatoapp.review.entity.Review;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
