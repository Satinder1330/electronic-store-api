package com.electronic.store.controllers;

import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.OrderInfoRequest;
import com.electronic.store.helper.ApiCustomResponse;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.OrderUpdateInfo;
import com.electronic.store.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/addOrder")
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderInfoRequest request){
        OrderDto orderDto = orderService.createOrder(request);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<ApiCustomResponse> delete(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
        return  new ResponseEntity<>( new ApiCustomResponse("Your order has been deleted successfully",HttpStatus.OK),HttpStatus.OK);
    }


    @GetMapping("/getSingle")
    public ResponseEntity<OrderDto> get(@RequestParam String userId,@RequestParam String orderId) {
        OrderDto orderDto = orderService.getSingle(userId, orderId);
        return  new ResponseEntity<>(orderDto,HttpStatus.OK);

    }
    //all orders of single user
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<CustomPaginationResponse<OrderDto> >AllOrdersOfUser(@PathVariable String userId,
                                                                                   @RequestParam int pageNumber,
                                                                                   @RequestParam int pageSize,
                                                                                   @RequestParam String sortBy,
                                                                                   @RequestParam String sortDir){
        CustomPaginationResponse<OrderDto> response = orderService.getAllOfUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    //get all the orders
    @GetMapping("/getAll")
    public ResponseEntity<CustomPaginationResponse<OrderDto> >AllOrders(@RequestParam int pageNumber,
                                                                        @RequestParam int pageSize,
                                                                        @RequestParam String sortBy,
                                                                        @RequestParam String sortDir){
        CustomPaginationResponse<OrderDto> response = orderService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //update status
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDto> update(@PathVariable String orderId,
                                                    @RequestBody OrderUpdateInfo orderUpdateInfo){
        OrderDto orderDto = orderService.updateStatus(orderUpdateInfo, orderId);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);
    }
}
