package com.example.paymentservice.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private int orderId;
    private int userId;
    private String message;
    private Set<OrderDetailDto> orderDetails = new HashSet<>();
    private double totalPrice;
    private String queueName;
    private String paymentStatus;
    private String inventoryStatus;
    private String orderStatus;

    public boolean validationPayment(){
        return this.totalPrice > 0 && this.orderId != 0 && this.userId != 0 && this.paymentStatus != null;
    }
}
