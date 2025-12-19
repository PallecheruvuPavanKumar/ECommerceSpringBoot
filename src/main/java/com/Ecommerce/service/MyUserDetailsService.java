package com.Ecommerce.service;

import com.Ecommerce.model.User;
import com.Ecommerce.model.UserPrincipal;
import com.Ecommerce.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRegistrationRepo;
    
    @Override
    public UserDetails loadUserByUsername(String userName){
        System.out.println("Entered");
        User byUserName = userRegistrationRepo.findByusername(userName);
        if (byUserName == null) {
            System.out.println("user is null");
            throw new UsernameNotFoundException("User not found: " + byUserName);
        }
        return new UserPrincipal(byUserName);
    }
}
