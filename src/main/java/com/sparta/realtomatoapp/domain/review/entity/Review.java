package com.sparta.realtomatoapp.domain.review.entity;

import com.sparta.realtomatoapp.domain.CreateAuditingEntity;
import com.sparta.realtomatoapp.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review extends CreateAuditingEntity {
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
