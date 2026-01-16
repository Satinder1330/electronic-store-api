package com.electronic.store.helper;

import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiCustomResponse {
    private String message;
    private HttpStatus status;

}
