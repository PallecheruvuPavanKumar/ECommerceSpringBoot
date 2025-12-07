package com.Ecommerce.service;

import com.Ecommerce.model.Product;
import com.Ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    
    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageFile(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return productRepo.save(product);
    }
    
    public void deleteProduct(int id) throws IOException {
        productRepo.deleteById(id);
    }
    
    public List<Product> searchProducts(String keyword){
        return productRepo.searchProducts(keyword);
    }
}
