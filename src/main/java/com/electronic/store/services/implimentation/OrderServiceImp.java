package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.OrderInfoRequest;
import com.electronic.store.entities.*;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.Helper;
import com.electronic.store.helper.OrderUpdateInfo;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.OrderRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImp implements OrderService {


    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ModelMapper mapper;
    private final Helper helper;

    public OrderServiceImp(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository, ModelMapper mapper, Helper helper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
        this.helper = helper;
    }

    @Override
    public OrderDto createOrder(OrderInfoRequest orderInfoRequest) {
        String userId = orderInfoRequest.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id does not exist!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundExc("Cart is empty, add items in the cart to proceed !"));

       //set order from orderDto
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .orderDate(new Date())
                .orderStatus(orderInfoRequest.getOrderStatus())
                .billingAddress(orderInfoRequest.getBillingAddress())
                .billingName(orderInfoRequest.getBillingName())
                .billingPhone(orderInfoRequest.getBillingPhone())
                .deliverdDate(null)
                .user(user)
                .build();
      // get items from cart and add to orderItem list then add orderItem in order
        List<CartItem> cartItems = cart.getItems();
        List<OrderItem>orderItems = new ArrayList<>();
        AtomicReference<Double> orderAmount =new AtomicReference<>(0.0);
        cartItems.forEach(item ->{
            OrderItem orderItem = OrderItem.builder()
                    .quantity(item.getQuantity())
                    .totalPrice(item.getQuantity() * item.getProduct().getDiscountedPrice())
                    .product(item.getProduct())
                    .order(order)
                    .build();
            orderItems.add(orderItem);
           orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());// to get the total order amount of all items
        });
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        cart.getItems().clear();// clear cart and then update database
        cartRepository.save(cart);
        if(order.getPaymentStatus()==null)order.setPaymentStatus("NOT PAID");
        if (order.getOrderStatus()==null)order.setOrderStatus("PENDING");
        Order saved = orderRepository.save(order);
        return mapper.map(saved, OrderDto.class);

    }

    @Override
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundExc("No order is present of the given id !"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto getSingle(String userId, String orderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id does not exist!"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundExc("No order is present of the given id !"));

        return mapper.map(order, OrderDto.class);
    }

//get all orders of a user
    @Override
    public CustomPaginationResponse<OrderDto> getAllOfUser(String userId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExc("User of the given id does not exist!"));
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findByUser(pageable, user);
        List<Order> content = page.getContent();
        List<OrderDto> orderDtos = new ArrayList<>();
        content.forEach(order -> {
            OrderDto map = mapper.map(order, OrderDto.class);
            orderDtos.add(map);
        });
        return helper.getPageableResponse(page, orderDtos, pageNumber, pageSize);
    }

    @Override
    public CustomPaginationResponse<OrderDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        List<Order> content = page.getContent();
        List<OrderDto>orderDtos = new ArrayList<>();
       content.forEach(order-> {
           orderDtos.add(mapper.map(order,OrderDto.class));
       });
        return helper.getPageableResponse(page, orderDtos, pageNumber, pageSize);
    }

    @Override
    public OrderDto updateStatus(OrderUpdateInfo orderUpdateInfo , String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundExc("No order is present of the given id !"));
        if(orderUpdateInfo.getOrderStatus().equalsIgnoreCase("dispatched"))order.setOrderStatus("DISPATCHED");
        if(orderUpdateInfo.getOrderStatus().equalsIgnoreCase("delivered"))order.setOrderStatus("DELIVERED");
        if(orderUpdateInfo.getPaymentStatus().equalsIgnoreCase("PAID"))order.setPaymentStatus("PAID");
        orderRepository.save(order);

        return mapper.map(order,OrderDto.class);
    }
}
