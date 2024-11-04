package com.sparta.realtomatoapp.menu.entity;

import com.sparta.realtomatoapp.store.entity.Store;
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
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "storeId")
    private Store store;
}
