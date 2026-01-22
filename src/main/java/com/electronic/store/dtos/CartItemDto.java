package com.electronic.store.dtos;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CartItemDto {
    private int cartItemId;
    private int quantity;
    private Double  totalPrice;
    private ProductDto product;

}
