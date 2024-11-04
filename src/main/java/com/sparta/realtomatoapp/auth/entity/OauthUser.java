package com.sparta.realtomatoapp.auth.entity;

import com.sparta.realtomatoapp.common.entity.BaseAuditingEntity;
import com.sparta.realtomatoapp.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUser extends BaseAuditingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
