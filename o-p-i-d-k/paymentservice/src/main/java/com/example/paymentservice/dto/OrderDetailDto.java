package com.example.paymentservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDto {
    private int productId;
    private int orderId;
    private String productName;
    private int quantity;
    private double unitPrice;

}
