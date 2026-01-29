package com.electronic.store.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    Logger logger  = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //header(authorization) = Bearer Token
        String requestHeader = request.getHeader("Authorization");
        logger.info("header - {}",requestHeader);
        String token = null;
        String username = null;
        if(requestHeader!=null && requestHeader.startsWith("Bearer")){

            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token);
                 logger.info("Username - {}",username);

            } catch (ExpiredJwtException ex){
                logger.error("Given jwt is expired {}",ex.getMessage());
            }catch (JwtException | IllegalArgumentException ex){
                logger.error("Some info has been changed in the token {}",ex.getMessage());
            }

        }else{
            logger.error("Header is not present");
        }
        //to check if user is already authenticated or not
        if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //match the token
            if(userDetails.getUsername().equals(username) && !jwtHelper.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //process to next default filters
        filterChain.doFilter(request,response);
    }
}
