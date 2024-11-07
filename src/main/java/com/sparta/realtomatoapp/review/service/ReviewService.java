package com.sparta.realtomatoapp.review.service;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.order.repository.OrderRepository;
import com.sparta.realtomatoapp.review.dto.ReviewCreateRequestDto;
import com.sparta.realtomatoapp.review.dto.ReviewCreateResponseDto;
import com.sparta.realtomatoapp.review.dto.ReviewListResponseDto;
import com.sparta.realtomatoapp.review.entity.Review;
import com.sparta.realtomatoapp.review.repository.ReviewRepository;
import com.sparta.realtomatoapp.store.entity.Store;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewCreateResponseDto createReview(Long orderId, ReviewCreateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        
        Review review = requestDto.toEntity();
        review.setOrder(order);

        Review savedReview = reviewRepository.save(review);
        return savedReview.toResponseDto(order);
    }

    public List<ReviewListResponseDto> getAllReviews(AuthUser authUser, Long storeId) {
//        OrderRepository에서 storeId로 해당 주문 목록을 조회합니다.
        Store store = Store.builder().storeId(storeId).build();
        User user = User.builder().userId(authUser.getUserId()).build();

        List<Order> orders = orderRepository.findAllByStoreAndUser(store, user);


        List<ReviewListResponseDto> reviews = new ArrayList<>();

        for (Order order : orders) {

            List<Review> reviewsByOrder = order.getReviews();
            for (Review review : reviewsByOrder) {
                ReviewListResponseDto responseDto = review.toListResponseDto(order);
                reviews.add(responseDto);
            }
        }

        return reviews;

    }

    public BaseResponseDto deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
        reviewRepository.deleteById(reviewId);

        return BaseResponseDto.baseResponseBuilder()
                .message("Review deleted")
                .build();
    }
}
