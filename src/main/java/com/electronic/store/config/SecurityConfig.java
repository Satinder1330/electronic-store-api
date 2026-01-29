package com.electronic.store.config;

import com.electronic.store.security.JwtAuthenticationEntryPoint;
import com.electronic.store.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter filter;
    private final JwtAuthenticationEntryPoint entryPoint;

    public SecurityConfig(JwtAuthenticationFilter filter, JwtAuthenticationEntryPoint entryPoint) {
        this.filter = filter;
        this.entryPoint = entryPoint;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity){
        httpSecurity.csrf(csrf -> csrf.disable());
     httpSecurity.authorizeHttpRequests(request -> {
         request.requestMatchers(HttpMethod.DELETE,"/user/**").hasRole(AppConstants.ROLE_ADMIN)
                 .requestMatchers(HttpMethod.PUT,"/user/**").hasAnyRole(AppConstants.ROLE_USER,AppConstants.ROLE_ADMIN)
                 .requestMatchers(HttpMethod.POST,"/user/**").permitAll()
                 .requestMatchers(HttpMethod.GET,"/user/**").permitAll()
                 .requestMatchers(HttpMethod.GET,"/product/**").permitAll()
                 .requestMatchers("/product/**").hasRole(AppConstants.ROLE_ADMIN)
                 .requestMatchers(HttpMethod.GET,"/category/**").permitAll()
                 .requestMatchers("/category/**").hasRole(AppConstants.ROLE_ADMIN)
                 .requestMatchers(HttpMethod.POST,"/auth/generate-token").permitAll()
                 .requestMatchers("/auth/**").authenticated()
                 .anyRequest().permitAll();

     });
     //basic security
     // httpSecurity.httpBasic(Customizer.withDefaults());

        //Jwt configuration
        // entry point (JwtAuthenticationEntryPoint
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint));
        //session creation policy
        httpSecurity.sessionManagement(ex-> ex.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       //Main filter before other filters
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    //password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

