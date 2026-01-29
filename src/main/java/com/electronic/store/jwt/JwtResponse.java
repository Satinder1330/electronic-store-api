package com.electronic.store.jwt;

import com.electronic.store.dtos.UserDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    UserDto user;
    private String refreshToken;


}
