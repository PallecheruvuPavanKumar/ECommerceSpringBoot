package com.Ecommerce.configuration;

import com.Ecommerce.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@NoArgsConstructor
public class SecurityConfig {
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    @Autowired
    JwtFilter jwtFilter;
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return authenticationProvider;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(customizer -> customizer.requestMatchers("/login", "/register").permitAll().anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, ex2) -> {
                            System.out.println("AUTHENTICATION FAILED: " + ex2.getMessage());
                            System.out.println("Request URI: " + req.getRequestURI());
                            System.out.println("Auth Header: " + req.getHeader("Authorization"));
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + ex2.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((req, res, ex2) -> {
                            System.out.println("ACCESS DENIED: " + ex2.getMessage());
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"" + ex2.getMessage() + "\"}");
                        })
                )
                .build();
    }
}
