package com.jojoldu.book.springboot.entities;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    public Optional<ItemEntity> findById(Long id);
}
