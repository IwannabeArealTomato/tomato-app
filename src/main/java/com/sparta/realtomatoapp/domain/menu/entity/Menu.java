package com.sparta.realtomatoapp.domain.menu.entity;

import com.sparta.realtomatoapp.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menus") // 예약어 충돌 방지
public class Menu {

    /**
    * Menu 엔터티에서 발생할 수 있는 몇 가지 잠재적인 문제와 개선 사항을 반영하여 수정하겠습니다.
    * 수정해야 할 잠재적 오류 및 개선 사항
    * 컬럼명 충돌:
    * storeId는 데이터베이스의 테이블 컬럼명으로 사용되므로, 일관성을 위해 -> store_id
    * 연관 관계 설정 (@ManyToOne 애노테이션):
    * menu 테이블 네이밍:
    * menu가 예약어로 사용될 수 있으므로, 안전을 위해 테이블명을 menus로 변경합니다.
    **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id", nullable = false) // 컬럼명 수정 및 nullable 설정
    private Store store;
}
