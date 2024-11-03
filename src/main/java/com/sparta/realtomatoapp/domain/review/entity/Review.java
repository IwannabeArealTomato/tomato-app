package com.sparta.realtomatoapp.domain.review.entity;

import com.sparta.realtomatoapp.domain.CreateAuditingEntity;
import com.sparta.realtomatoapp.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews") // 예약어 충돌 방지
public class Review extends CreateAuditingEntity {

    /**
     * 테이블명 충돌:
     * review는 예약어와의 충돌을 방지하기 위해 reviews로 변경함
     * 연관 관계 설정 (@ManyToOne 애노테이션):
     * orderId 필드는 컬럼명으로 사용할 때 일관성을 위해 order_id로 변경하는 것이 좋습니다.
     * scope 필드는 사용 목적이 점수를 나타내는 것이라면, rating과 같은 명확한 이름으로 변경할 수 있다고 GPT가 말해서 변경해봄
     * 범위 검증 추가:
     * rating 값이 1~5 범위 내에서만 설정되도록 검증 로직을 추가하는 것도 좋은 방법이라고 하더라
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer rating; // 필드명 변경 (scope -> rating)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // 컬럼명 수정 및 nullable 설정
    private Order order;
}
