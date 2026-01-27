package com.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;
@Size(min=3,max=25,message = "Name size should be between 3 to 15")
    private String name;

@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "email Pattern is not valid")
@NotBlank(message = "Email is required")
    private String email;
@NotBlank(message = "Password can not be null")
    private  String password;

@Size(min=4,message = "Enter valid gender")
    private String gender;

    private String imageName;
    private List<RoleDto> roles;
}
