package com.electronic.store.config;

import com.electronic.store.security.JwtAuthenticationEntryPoint;
import com.electronic.store.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
//@EnableWebMvc
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    public SecurityConfig(JwtAuthenticationFilter filter, JwtAuthenticationEntryPoint entryPoint) {
        this.jwtFilter = filter;
        this.entryPoint = entryPoint;
    }

    private final String[] public_urls={"/swagger-ui/**","/webjars/**","/swagger-resources/**","/v3/api-docs"};

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
                 .requestMatchers(HttpMethod.POST,"/auth/generate-token","/auth/google-login","/auth/regenerate-jwt-token").permitAll()
                 .requestMatchers("/auth/**").authenticated()
                 .requestMatchers(public_urls).permitAll()
                 .anyRequest().permitAll();

     });
     //basic security
     // httpSecurity.httpBasic(Customizer.withDefaults());

        //Jwt configuration
        // entry point (JwtAuthenticationEntryPoint)
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint));
        //session creation policy
        httpSecurity.sessionManagement(ex-> ex.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       //Main filter before other filters
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // FOR CORS if needed
        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public @Nullable CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:8000","http://localhost:4000"));
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(List.of("*"));
                return corsConfiguration;
            }
        }) );
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

