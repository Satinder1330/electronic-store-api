package com.electronic.store.controllers;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.jwt.JwtRequest;
import com.electronic.store.jwt.JwtResponse;
import com.electronic.store.security.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

private final JwtHelper jwtHelper;
private final UserDetailsService userDetailsService;
private final ModelMapper mapper;
private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtHelper jwtHelper, UserDetailsService userDetailsService, ModelMapper mapper, AuthenticationManager authenticationManager) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> create(@RequestBody JwtRequest jwtRequest){
        logger.info("username - {},password-{}",jwtRequest.getEmail(),jwtRequest.getPassword());
      //authenticate first
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtRequest.getEmail() ,jwtRequest.getPassword());
            authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("Bad credentials");
        }

        User user =(User) userDetailsService.loadUserByUsername(jwtRequest.getEmail());

        String token = jwtHelper.generateToken(user);
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .user(mapper.map(user, UserDto.class))
                .build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

    }

}
