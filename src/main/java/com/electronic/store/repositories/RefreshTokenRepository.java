package com.electronic.store.repositories;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.RefreshToken;
import com.electronic.store.entities.User;
import io.jsonwebtoken.security.Jwks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken>findByToken(String token);
    Optional<RefreshToken>findByUser(User user);
  //  Optional<UserDto>getUser(RefreshTokenDto refreshTokenDto);
}
