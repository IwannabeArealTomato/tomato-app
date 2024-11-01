package com.sparta.realtomatoapp.domain.menu.repository;

import com.sparta.realtomatoapp.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
