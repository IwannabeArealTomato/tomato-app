package com.sparta.realtomatoapp.domain.review.entity;

import com.sparta.realtomatoapp.common.BaseAuditingEntity;
import com.sparta.realtomatoapp.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Integer scope;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
