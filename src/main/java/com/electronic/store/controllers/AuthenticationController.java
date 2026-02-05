package com.electronic.store.controllers;

import com.electronic.store.dtos.GoogleLoginRequest;
import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.jwt.JwtRequest;
import com.electronic.store.jwt.JwtResponse;
import com.electronic.store.security.JwtHelper;
import com.electronic.store.services.RefreshTokenService;
import com.electronic.store.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller",description = "APIs for authentications")
public class AuthenticationController {
    @Autowired
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

@Value("${google.oauth.client-id}")
private String googleClientId;
    @Value("${google.oauth.client-password}")
    private String googleClientPassword;

private final JwtHelper jwtHelper;
private final UserDetailsService userDetailsService;
private final ModelMapper mapper;
private final AuthenticationManager authenticationManager;
@Autowired
private RefreshTokenService refreshTokenService;

    public AuthenticationController(JwtHelper jwtHelper, UserDetailsService userDetailsService, ModelMapper mapper, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> create(@RequestBody JwtRequest jwtRequest){
        logger.info("username - {},password-{}",jwtRequest.getEmail(),jwtRequest.getPassword());
      //authenticate first
        JwtResponse jwtResponse = authenticateAndGenerateToken(jwtRequest.getEmail(), jwtRequest.getPassword());

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }


    //for google login
    @PostMapping("/google-login")
    public ResponseEntity<JwtResponse> handleWithGoogle(@RequestBody GoogleLoginRequest request) throws GeneralSecurityException, IOException {
             logger.info("google login token :-{}",request.getIdToken());
        //verify token with google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(),new GsonFactory()).setAudience(List.of(googleClientId)).build();
        GoogleIdToken googleToken = verifier.verify(request.getIdToken());
        if(googleToken!= null){
            GoogleIdToken.Payload payload = googleToken.getPayload();
            //get all the info you need from the payload
            String email = payload.getEmail();
            String userName = payload.getSubject();
            Boolean emailVerified = payload.getEmailVerified();
            logger.info("email {}, userName {}",email,userName);
            //add user info in our database
            UserDto userdto = new UserDto();
            //set all the info from payload to our user
           // userdto.setName(payload.getName);
            userdto.setEmail(email);
            userdto.setPassword(googleClientPassword);
            //userdto.setProvider(payload.getProvider); // make sure to add provider from the frontend
            //if already available or not
            UserDto user=null;
            try {
                 user = userService.getUserByEmail(userdto.getEmail());

                 if(user.getProvider().equals(userdto.getProvider())){
                     //continue -  provider matched
                 }else{
                     throw new BadCredentialsException("Email is already registered , try to login with your email and password");
                 }
            }
            catch(ResourceNotFoundExc ex) {
                logger.info(" New user so creating a new  User");
                user = userService.createUser(userdto);
            }
            User user1 = mapper.map(user, User.class);

            JwtResponse jwtResponse = authenticateAndGenerateToken(user.getEmail(), googleClientPassword); // real password not the encoded one from the database

            return  new ResponseEntity<>(jwtResponse,HttpStatus.OK);
        }else
        {
            logger.info("Google Token is Not Valid !!");
            throw new BadRequestException("Invalid google User !! ");
        }
    }
    @PostMapping("/regenerate-jwt-token")
    public  ResponseEntity<JwtResponse> regenerateJwt(@RequestParam String refreshToken ){
        RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(refreshToken);
        RefreshTokenDto refreshTokenDto1 = refreshTokenService.verifyRefreshToken(refreshTokenDto);
        UserDto userDto = refreshTokenDto1.getUser();
        String generatedToken = jwtHelper.generateToken(mapper.map(userDto, User.class));
        JwtResponse jwtResponse = JwtResponse.builder().token(generatedToken).user(userDto).refreshToken(refreshTokenDto).build();
        return  ResponseEntity.ok(jwtResponse);


    }


     public JwtResponse authenticateAndGenerateToken(String email,String password){
         try {
             Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
             authenticationManager.authenticate(authentication);
         }catch (BadCredentialsException ex){
             throw new BadCredentialsException("Bad credentials");
         }

         User user =(User) userDetailsService.loadUserByUsername(email);

         String token = jwtHelper.generateToken(user);
         //add refresh token
         RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

         JwtResponse jwtResponse = JwtResponse.builder()
                 .token(token)
                 .user(mapper.map(user, UserDto.class))
                 .refreshToken(refreshToken)
                 .build();
         return jwtResponse;
     }
}
