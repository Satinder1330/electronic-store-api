package com.electronic.store.serviceTest;

import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CartServiceTest {
    @MockitoBean
    private CartRepository cartRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartService cartService;

    Cart cart;
    User user;
    Product product;
    CartItem cartItem;

    List<CartItem>list;
    @BeforeEach
    public void init(){
        product = Product.builder().productId("123").productName("Iphone").productDescription("Blue color").price(999.0).discountedPrice(900.0).quantity(10)
                .addedDate(new Date()).live(true).productImage("abc.jpg").build();
        user = User.builder().userId("12").name("Mark").email("Mark123@gmail.com").password("Mark").gender("male").imageName("abc.jpg").build();
        cartItem = CartItem.builder().product(product).quantity(1).totalPrice(322).cart(new Cart()).build();
        list = new ArrayList<>(List.of(cartItem));
        cart = Cart.builder().createdAt(new Date()).user(user).items(list).build();


    }

    @Test
    public void addItemToCart() throws BadRequestException {
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(Mockito.any())).thenReturn(Optional.of(cart));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        CartDto cartDto = cartService.addItemToCart("12", "123", 2);
        Assertions.assertEquals(cart.getUser().getName(),cartDto.getUser().getName());

    }

    @Test
    public void removeItemFromCartTest(){
        Mockito.when(cartItemRepository.findById(Mockito.any())).thenReturn(Optional.of(cartItem));
        cartService.removeItemFromCart("54",2);
        Mockito.verify(cartItemRepository,Mockito.times(1)).delete(cartItem);

    }
    @Test
    public  void clearCartTest(){
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
      //  Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        cartService.clearCart("324");
        Mockito.verify(cartRepository,Mockito.times(1)).save(Mockito.any());


    }
    @Test
    public void getCartByUserTest(){
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        CartDto cartByUser = cartService.getCartByUser("231");
        Assertions.assertEquals(cart.getUser().getName(),cartByUser.getUser().getName());
    }

}
