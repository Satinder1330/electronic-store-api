package com.electronic.store.services;

import com.electronic.store.dtos.CartDto;

public interface CartService {
    CartDto addItemInCart(String userId,String productId,int quantity);//create cart if not present then add or add item if already present
    void deleteItem(String userId,int cartItemId);
    void clearCart(String userId);
    CartDto getCartOfUser(String userId);

}
