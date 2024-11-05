package com.sparta.realtomatoapp.store.repository;

import com.sparta.realtomatoapp.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
