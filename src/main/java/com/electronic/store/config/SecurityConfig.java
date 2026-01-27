package com.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity){
        httpSecurity.csrf(csrf -> csrf.disable());
     httpSecurity.authorizeHttpRequests(request -> {
         request.requestMatchers(HttpMethod.DELETE,"/user/**").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.PUT,"/user/**").hasAnyRole("USER","ADMIN")
                 .requestMatchers(HttpMethod.POST,"/user/**").permitAll()
                 .requestMatchers(HttpMethod.GET,"/user/**").permitAll()
                 .requestMatchers(HttpMethod.GET,"/product/**").permitAll()
                 .requestMatchers("/product/**").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.GET,"/category/**").permitAll()
                 .requestMatchers("/category/**").hasRole("ADMIN")
                 .anyRequest().permitAll();

     });
     httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
    //password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
