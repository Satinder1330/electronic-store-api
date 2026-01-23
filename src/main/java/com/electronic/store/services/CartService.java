package com.electronic.store.services;

import com.electronic.store.dtos.CartDto;
import org.apache.coyote.BadRequestException;

public interface CartService {
    CartDto addItemToCart(String userId, String productId, int quantity) throws BadRequestException;

    void removeItemFromCart(String userId,int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);

}
