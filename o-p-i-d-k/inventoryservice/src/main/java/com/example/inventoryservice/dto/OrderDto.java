package com.example.inventoryservice.dto;

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

    public boolean validationInventory(){
        return this.orderId != 0 && this.orderStatus != null && this.inventoryStatus != null;
    }
}
