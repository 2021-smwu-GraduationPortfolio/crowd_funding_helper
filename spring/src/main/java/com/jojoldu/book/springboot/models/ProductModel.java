package com.jojoldu.book.springboot.models;

import java.util.ArrayList;
import java.util.List;
import com.jojoldu.book.springboot.entities.Product;
import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;



public class ProductModel {


    @Autowired
    ProductService productService;

    private List<ProductEntity> products;



    public List<ProductEntity> findAll() {
        this.products = productService.findAll();
        System.out.println("product model find all : "+products);
        return products;
    }

    public ProductEntity find(Long id) {
        for (ProductEntity product : this.products) {
            if (product.getId()==id) {
                return product;
            }
        }
        return null;
    }

}