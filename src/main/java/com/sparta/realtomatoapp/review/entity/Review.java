package com.sparta.realtomatoapp.review.entity;

import com.sparta.realtomatoapp.common.entity.BaseAuditingEntity;
import com.sparta.realtomatoapp.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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