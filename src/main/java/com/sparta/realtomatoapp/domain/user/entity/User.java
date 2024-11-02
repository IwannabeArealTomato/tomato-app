package com.sparta.realtomatoapp.domain.user.entity;

import com.sparta.realtomatoapp.domain.order.entity.Order;
import com.sparta.realtomatoapp.domain.store.entity.Store;
import com.sparta.realtomatoapp.domain.user.common.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(updatable = false)
    private LocalDate modifiedAt;

    @OneToMany
    private List<Order> orders;

    @OneToMany
    private List<Store> stores;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // UserRole Enum을 사용
}
