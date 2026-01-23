package com.electronic.store.services;

import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.OrderInfoRequest;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.OrderUpdateInfo;

public interface OrderService {
    //create order
    OrderDto createOrder (OrderInfoRequest orderInfoRequest);
    //remove
    void deleteOrder(String orderId);
    //get single order of user
    OrderDto getSingle(String userId,String orderId);
    //allOrders of a user
    CustomPaginationResponse<OrderDto> getAllOfUser(String userId,int pageNumber,int pageSize,String sortBy,String sortDir);
    //all orders
     CustomPaginationResponse<OrderDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    //update status
    OrderDto updateStatus(OrderUpdateInfo orderUpdateInfo , String orderId);

}
