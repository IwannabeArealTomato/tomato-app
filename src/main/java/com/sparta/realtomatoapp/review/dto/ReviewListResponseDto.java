package com.sparta.realtomatoapp.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListResponseDto {

    private Long reviewId;
    private Long orderId;
    private String comment;
    private int rating;

}
