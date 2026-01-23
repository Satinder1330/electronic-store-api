package com.electronic.store.helper;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateInfo {
    private String orderStatus;
    private String paymentStatus;
}
