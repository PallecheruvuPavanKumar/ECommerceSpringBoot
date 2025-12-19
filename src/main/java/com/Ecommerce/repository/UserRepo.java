package com.Ecommerce.repository;

import com.Ecommerce.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo {
    
    User findByusername(String userName);
}
