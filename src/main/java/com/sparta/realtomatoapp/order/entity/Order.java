package com.sparta.realtomatoapp.domain.order.entity;

import com.sparta.realtomatoapp.domain.CreateAuditingEntity;
import com.sparta.realtomatoapp.domain.order.service.Status;
import com.sparta.realtomatoapp.domain.store.entity.Store;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer amount;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;
}
