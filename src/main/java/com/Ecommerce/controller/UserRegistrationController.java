package com.Ecommerce.controller;



import com.Ecommerce.model.User;
import com.Ecommerce.service.JwtService;
import com.Ecommerce.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRegistrationController {
    
    @Autowired
    private UserRegistrationService userRegistrationService;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping({"/register"})
    public ResponseEntity<User> newUserRegistration(@RequestBody User user) {
        User savedUser = userRegistrationService.createUser(user);
        System.out.println("created");
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    @PostMapping({"/login"})
    public ResponseEntity<Map<String,String>> login(@RequestBody User user) {
        System.out.println("Controller hit: " + user.getUsername());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authenticate.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUsername());
            Map<String,String> res = new HashMap<>();
            res.put("token", token);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
}
