package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Product;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ProductService {
    Page getAll(int page, int pageSize);

    Product findById(int id);

    Product save(Product product);

    void saveAll(Set<Product> productSet);
}
