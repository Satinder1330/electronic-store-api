package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.RefreshToken;
import com.electronic.store.entities.User;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.repositories.RefreshTokenRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImp implements RefreshTokenService {
    public RefreshTokenServiceImp(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mapper = mapper;
    }

    private  UserRepository userRepository;
    private ModelMapper mapper;
    private RefreshTokenRepository refreshTokenRepository;
    private Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImp.class);

    @Override
    public RefreshTokenDto createRefreshToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundExc("Useer of the given username does not exist!!"));
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
        //create if not present
        if(refreshToken==null) {
            refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(24 * 60 * 60))
                    .user(user).
                    build();
        }
        //change the token for better safety
        else{
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(24 * 60 * 60));
        }
        refreshTokenRepository.save(refreshToken);
        RefreshTokenDto refreshTokenDto = mapper.map(refreshToken, RefreshTokenDto.class);

        return refreshTokenDto;
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundExc("No data for the given token is present"));

        return mapper.map(refreshToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = mapper.map(refreshTokenDto, RefreshToken.class);

        if(refreshTokenDto.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
          logger.error("Refresh Token is expired");
        }
        return refreshTokenDto;
    }

//    @Override
//    public UserDto getUser(RefreshTokenDto refreshTokenDto) {
//        UserDto userDto = refreshTokenRepository.getUser(refreshTokenDto).orElseThrow(() -> new ResourceNotFoundExc("No user found related to the refresh token !!"));
//
//        return userDto;
//    }
}
