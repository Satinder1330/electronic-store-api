package com.electronic.store.jwt;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JwtRequest {
    private String email;
    private String password;
}
