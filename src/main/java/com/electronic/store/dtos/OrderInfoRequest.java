package com.electronic.store.dtos;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoRequest {
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private String billingAddress;
    private String billingPhone;
    private String billingName;
}
