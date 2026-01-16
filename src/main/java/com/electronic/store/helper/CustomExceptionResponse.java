package com.electronic.store.helper;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionResponse {
    private String message;
    private HttpStatus status;


}
