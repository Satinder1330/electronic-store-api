package com.electronic.store.dtos;

import com.electronic.store.entities.User;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {
    private String token;
    private Instant expiryDate;
    @OneToOne
    private UserDto user;
}
