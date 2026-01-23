package com.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private Double totalPrice;
    private ProductDto product;
}
