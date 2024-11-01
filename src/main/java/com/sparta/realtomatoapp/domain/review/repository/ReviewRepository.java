package com.sparta.realtomatoapp.domain.review.repository;

import com.sparta.realtomatoapp.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
