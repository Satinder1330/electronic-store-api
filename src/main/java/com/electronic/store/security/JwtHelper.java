package com.electronic.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
// for jwt operations
public class JwtHelper {


    //validity
    public  static final long  TOKEN_VALIDITY = 5*60*1000;

    public static final String SECRET_KEY = "qtydgafaufusdyfgdshfashiufyuasfghfioyqewfggsgvlisudfiuyw";

    byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
    SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);      // to match with signature algorithm hs256

    //generate Token
    //get userDetails from userName then add in this method
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims= new HashMap<>();
        String username = userDetails.getUsername();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(secretKey)      // no need to use signature algorithm here
                .compact();
    }

    //get all the claims from token
    public Claims getAllClaimsFromToken(String token){
return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    }
    // get the userName of a Token
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    //get the expiration date of a token
    public Date expirationDateOfToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

}
