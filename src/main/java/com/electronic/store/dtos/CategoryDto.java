package com.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;
    @Size(min = 2,max = 100,message = "category size should be between 2 to 100")

    private String title;

    @NotBlank
    private String description;

    private String coverImage;
}
