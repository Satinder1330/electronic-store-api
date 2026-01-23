package com.electronic.store.dtos;

import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private Double orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate=new Date();
    private Date deliverdDate;
    private UserDto user;
    List<OrderItemDto> orderItems= new ArrayList<>();
}
