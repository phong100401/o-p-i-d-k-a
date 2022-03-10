package com.example.orderservice.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItem {
    private int productId;
    private String name;
    private int quantity;
    private double unitPrice;
}
