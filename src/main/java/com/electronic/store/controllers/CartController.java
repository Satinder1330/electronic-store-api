package com.electronic.store.controllers;

import com.electronic.store.config.AppConstants;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.helper.ApiCustomResponse;
import com.electronic.store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Cart Controller",description = "APIs for Cart")
public class CartController {
    @Autowired
    private   CartService cartService;

    @PostMapping("/addItem")
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_USER+"','"+AppConstants.ROLE_ADMIN+"')")
    public ResponseEntity<CartDto> addItemToCart(@RequestParam String userId,
                                                 @RequestParam String productId,
                                                 @RequestParam int quantity) throws BadRequestException {
        CartDto cartDto = cartService.addItemToCart(userId, productId, quantity);
        return  new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    //delete item from the cart
    @DeleteMapping("/{userId}/delete/{cartItemId}")
    @PreAuthorize("hasRole('"+AppConstants.ROLE_ADMIN+"')")
    public ResponseEntity<ApiCustomResponse> deleteItem(@PathVariable String userId,@PathVariable int cartItemId){
        cartService.removeItemFromCart(userId,cartItemId);
        return new ResponseEntity<>(new ApiCustomResponse("Item has been deleted from your cart",HttpStatus.OK),HttpStatus.OK);
    }
    @DeleteMapping("/clearCart/{userId}")
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_USER+"','"+AppConstants.ROLE_ADMIN+"')")
    public ResponseEntity<ApiCustomResponse> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        return new ResponseEntity<>(new ApiCustomResponse("Cart of the user is now clear",HttpStatus.OK),HttpStatus.OK);

    }
    @GetMapping("/getCart/{userId}")
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_USER+"','"+AppConstants.ROLE_ADMIN+"')")
    public  ResponseEntity<CartDto> getCart(@PathVariable String userId){
        CartDto cartOfUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartOfUser,HttpStatus.OK);
    }

}
