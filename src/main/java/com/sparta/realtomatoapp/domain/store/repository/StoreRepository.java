package com.sparta.realtomatoapp.domain.store.repository;

import com.sparta.realtomatoapp.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
