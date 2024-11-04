package com.sparta.realtomatoapp.user.entity;

import com.sparta.realtomatoapp.common.entity.ModifiedAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status; // UserStatus Enum을 사용

    @Column(nullable = false)
    private String address;

}
