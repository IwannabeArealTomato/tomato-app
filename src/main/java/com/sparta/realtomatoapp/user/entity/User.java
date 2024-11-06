package com.sparta.realtomatoapp.user.entity;

import com.sparta.realtomatoapp.auth.entity.OauthUser;
import com.sparta.realtomatoapp.common.entity.ModifiedAuditingEntity;
import com.sparta.realtomatoapp.order.entity.Order;
import com.sparta.realtomatoapp.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.TrueFalseConverter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@SoftDelete(columnName = "deleted", converter = TrueFalseConverter.class)
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

    @Column(name="deleted", insertable = false, updatable = false)
    private String deleted;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OauthUser> oauthUsers;
}
