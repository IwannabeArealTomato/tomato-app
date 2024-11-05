package com.sparta.realtomatoapp.review.dto;

import lombok.Builder;

@Builder
public class ReviewCreateResponseDto {

private Long reviewId;
private Long orderId;
private String comment;
private int rating;

}
