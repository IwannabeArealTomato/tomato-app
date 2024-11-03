package com.sparta.realtomatoapp.domain.order.entity;

import com.sparta.realtomatoapp.domain.common.CreateAuditingEntity;
import com.sparta.realtomatoapp.domain.order.service.Status;
import com.sparta.realtomatoapp.domain.store.entity.Store;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders") // 예약어 충돌 방지
public class Order extends CreateAuditingEntity {

    /**
     * 수정해야 할 잠재적 오류 및 개선 사항
     * 테이블명 충돌:
     *
     * @Table(name = "order")에서 order는 SQL에서 예약어로 사용된다고함 그래서 order  __s__ 붙임
     *
     * 컬럼명 충돌:
     * userId, storeId, menuId 등의 이름이 테이블에 따라 다르게 사용될 수 있습니다. 일관성을 위해 user_id, store_id, menu_id 형식으로 이름을 수정합니다.
     *
     * 연관 관계 설정 (@ManyToOne 애노테이션):
     * @ManyToOne은 기본적으로 지연 로딩(FetchType.LAZY)을 사용하지만, 명시적으로 설정하는 것이 좋습니다.
     *
     * 상태 열거형 Status:
     * Status 열거형 사용해봄 필요가 있을듯 store 처럼 store는 해봄
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY) // 명시적으로 FetchType 설정
    @JoinColumn(name = "user_id", nullable = false) // 컬럼명 수정 및 nullable 설정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
}
