package com.Ecommerce.service;

import com.Ecommerce.model.User;
import com.Ecommerce.model.UserPrincipal;
import com.Ecommerce.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byusername = userRepo.findByusername(username);
        if(byusername !=null){
            new UserPrincipal(byusername);
        }
        else
            System.out.println("UserDetails Not Found in the DataBase");
        
        return null;
    }
}
