package com.sparta.realtomatoapp.review.repository;

import com.sparta.realtomatoapp.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
