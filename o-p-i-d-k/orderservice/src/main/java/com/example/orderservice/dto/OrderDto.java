package com.example.orderservice.dto;

import com.example.orderservice.entity.Order;
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

    public OrderDto(Order order){
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.paymentStatus = order.getPaymentStatus();
        this.inventoryStatus = order.getInventoryStatus();
        this.orderStatus = order.getOrderStatus();
        order.getOrderDetails().forEach(orderDetail -> {
            this.orderDetails.add(new OrderDetailDto(orderDetail));
        });
    }

    public boolean validationPayment(){
        return this.totalPrice > 0 && this.orderId != 0 && this.userId != 0 && this.paymentStatus != null;
    }

    public boolean validationInventory(){
        return this.orderId != 0 && this.orderStatus != null && this.inventoryStatus != null;
    }
}
