package com.Ecommerce.service;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.*;



@Service
public class JwtService {
    
    
    private static final String SECRET_KEY = "my-super-secret-key-my-super-secret-key";
    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of("ROLE_USER"));
        
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSigningKey())
                .compact();
    }
    
}
