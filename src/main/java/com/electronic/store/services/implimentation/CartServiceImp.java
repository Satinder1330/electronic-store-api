package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final  ModelMapper mapper;
    private final CartItemRepository cartItemRepository;

    public CartServiceImp(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, ModelMapper mapper,CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
        this.cartItemRepository=cartItemRepository;
    }

    @Override
    public CartDto addItemInCart(String userId, String productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id not found ! "));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExc("Product of the given id does not exist !"));
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart=null;
        try {
            cart = optionalCart.get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getCartItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }
    @Override
    public void deleteItem(String userId,int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundExc("CartItem of the given id is not present"));
       cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id is not present!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundExc("cart of the user not found"));
        cart.getCartItems().clear(); // clear items from the cart
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id is not present!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundExc("cart of the user not found"));

        return mapper.map(cart, CartDto.class);
    }
}
