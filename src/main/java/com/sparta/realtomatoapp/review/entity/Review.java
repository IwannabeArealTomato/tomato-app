package com.sparta.realtomatoapp.review.entity;

import com.sparta.realtomatoapp.common.entity.BaseAuditingEntity;
import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @Setter
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    public ReviewCreateResponseDto toResponseDto(Order order) {
        return ReviewCreateResponseDto
                .builder()
                .orderId(order.getOrderId())
                .reviewId(reviewId)
                .comment(comment)
                .rating(rating)
                .build();
    }

    public ReviewListResponseDto toListResponseDto(Order order) {
       return ReviewListResponseDto.builder()
                .orderId(order.getOrderId())
                .reviewId(reviewId)
                .comment(comment)
                .rating(rating)
                .build();

    }
}
