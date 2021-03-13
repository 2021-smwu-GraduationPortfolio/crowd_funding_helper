package com.jojoldu.book.springboot.entities;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    public Optional<ProductEntity> findById(Long id);


}
