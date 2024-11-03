package com.sparta.realtomatoapp.domain.store.entity;

import com.sparta.realtomatoapp.domain.store.common.StoreStatus;
import com.sparta.realtomatoapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stores") // 예약어 충돌 방지
public class Store {

    /**
     * 테이블명 충돌:
     * store는 예약어와의 충돌을 방지하기 위해 테이블명을 stores로 변경함
     * 연관 관계 설정 (@ManyToOne 애노테이션):
     * @JoinColumn에서 userId 필드명을 user_id로 변경하여 데이터베이스 컬럼명과 일관성을 유지합니다.
     * status 필드에 Enum 타입 도입:
     * status 필드가 매장 상태(예: OPEN, CLOSED)를 나타낸다면, Enum을 사용하여 상태 값을 관리하는 것이 좋다고함.
     * 예를 들어 StoreStatus라는 열거형을 정의하여 이를 통해 상태를 관리하라해서 StoreStatus라는 eunm 만들어봄
     * public enum StoreStatus {
     *     OPEN,
     *     CLOSED
     * } 이거로 만들어 놓고 common에 넣어둠
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Long minPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status; // Enum 타입으로 변경하여 관리

    @ManyToOne(fetch = FetchType.LAZY) // 명시적으로 지연 로딩 설정
    @JoinColumn(name = "user_id", nullable = false) // 컬럼명 수정 및 nullable 설정
    private User user;
}
