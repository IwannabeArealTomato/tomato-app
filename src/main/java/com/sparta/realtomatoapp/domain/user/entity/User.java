package com.sparta.realtomatoapp.domain.user.entity;


import com.sparta.realtomatoapp.domain.order.entity.Order;
import com.sparta.realtomatoapp.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

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
}
