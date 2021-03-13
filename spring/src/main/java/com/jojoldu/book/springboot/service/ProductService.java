package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.entities.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> findAll(){
        List<ProductEntity> products = new ArrayList<>();
        productRepository.findAll().forEach(e->products.add(e));
        //System.out.println("product service find all : "+products);
        return products;
    }

    public Optional<ProductEntity> findById(Long id){
        Optional<ProductEntity> product = productRepository.findById(id);
        return product;
    }

    public void deleteById(Long id){
        productRepository.deleteById(id);
    }

    public ProductEntity save(ProductEntity product){
        productRepository.save(product);
        return product;
    }

    public void updateById(Long id, ProductEntity product) {
        Optional<ProductEntity> e = productRepository.findById(id);
        if (e.isPresent()) {
            e.get().setId(product.getId());
            e.get().setTitle(product.getTitle());
            e.get().setPagename(product.getPagename());
            e.get().setUrl(product.getUrl());
            productRepository.save(product);
        }
    }

}
