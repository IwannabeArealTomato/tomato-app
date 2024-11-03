package com.sparta.realtomatoapp.domain.store.entity;

import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.menu.entity.Menu;
import com.sparta.realtomatoapp.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
public class Store {

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

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
