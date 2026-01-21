package com.electronic.store.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String productId;
  @NotBlank
  @Size(min = 2,max = 30)
    private String productName;
  @NotBlank
  @Size(min = 5)
    private String productDescription;
  @NotNull
    private Double price;
    private Double discountedPrice;
  @NotNull
    private Integer quantity;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
    private Date addedDate;
  @NotNull
    private Boolean live;
  @NotNull
    private  Boolean stock;

    private String productImage;

    private Set<String> categoryIds;

    private  Set<String> categoryNames;
}
