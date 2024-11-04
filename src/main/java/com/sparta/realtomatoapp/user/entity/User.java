package com.sparta.realtomatoapp.domain.user.entity;

import com.sparta.realtomatoapp.domain.BaseAuditingEntity;
import com.sparta.realtomatoapp.domain.order.entity.Order;
import com.sparta.realtomatoapp.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends ModifiedAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // UserRole Enum을 사용

    @Column(nullable = false)
    private String address;

}
