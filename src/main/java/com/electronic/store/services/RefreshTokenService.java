package com.electronic.store.services;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.RefreshToken;

public interface RefreshTokenService {
    RefreshTokenDto createRefreshToken(String username);
    RefreshTokenDto findByToken(String token);

   RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);
 //  UserDto getUser(RefreshTokenDto refreshTokenDto);

}
