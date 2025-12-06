package com.Ecommerce.service;

import com.Ecommerce.model.Product;
import com.Ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepo productRepo;
    
    public List<Product> getAllProducts() {
    return productRepo.findAll();
    }
    
    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }
}
