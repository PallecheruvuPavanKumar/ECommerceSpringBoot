package com.Ecommerce.configuration;

import com.Ecommerce.service.JwtService;
import com.Ecommerce.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request URI: " + request.getRequestURI());
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid Authorization header found - continuing without authentication");
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = authHeader.substring(7);
        String username;
        
        try {
            username = jwtService.extractUserName(token);
            System.out.println("Username extracted from token: " + username);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to extract username from token: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
        String path = request.getRequestURI();
        if (path.equals("/api/login") || path.equals("/api/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        else if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
           
            
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✓ Authentication successful for user: " + username);
            } else {
                System.out.println("✗ Token validation failed for user: " + username);
            }
            filterChain.doFilter(request, response);
        }
        
        System.out.println("Final Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("======================");
        
        
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/login") || path.equals("/api/register");
    }
    
}
