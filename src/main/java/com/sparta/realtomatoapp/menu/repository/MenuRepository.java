package com.sparta.realtomatoapp.menu.repository;

import com.sparta.realtomatoapp.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
