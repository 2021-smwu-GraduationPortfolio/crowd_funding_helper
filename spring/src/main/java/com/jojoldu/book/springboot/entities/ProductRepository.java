package com.jojoldu.book.springboot.entities;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    public Optional<ProductEntity> findById(Long id);

    @Query(value = "SELECT title FROM product where product.category=?1 order by product.achieve desc limit 10", nativeQuery = true)
    String[] findTitleTop10(String category);
}
