package com.jojoldu.book.springboot.models;

import java.util.ArrayList;
import java.util.List;
import com.jojoldu.book.springboot.entities.Product;

public class ProductModel {

    private List<Product> products;

    public ProductModel() {
        this.products = new ArrayList<Product>();
        this.products.add(new Product("p01", "name 1", 20));
        this.products.add(new Product("p02", "name 2", 21));
        this.products.add(new Product("p03", "name 3", 22));
    }

    public List<Product> findAll() {
        return this.products;
    }

    public Product find(String id) {
        for (Product product : this.products) {
            if (product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }
        return null;
    }

}