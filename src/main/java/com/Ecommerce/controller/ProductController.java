package com.Ecommerce.controller;

import com.Ecommerce.model.Product;
import com.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping({"products"})
    public ResponseEntity<List<Product>> getAllProducts(){
      return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    
    @GetMapping({"product/{id}"})
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product productById = productService.getProductById(id);
        if(productById != null) {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
