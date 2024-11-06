package com.sparta.realtomatoapp.security.refreshToken.entity;

import com.sparta.realtomatoapp.user.entity.User;
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
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenValue;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public void updateToken(String refreshToken) {
        this.tokenValue = refreshToken;
    }
}
