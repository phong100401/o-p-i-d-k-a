package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int productId;

    @ManyToOne
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    @JsonIgnore
    private Order order;


    private int orderId;

    private String productName;

    private int quantity;

    private double unitPrice;

    public OrderDetail(CartItem cartItem){
        this.productId = cartItem.getProductId();
        this.productName = cartItem.getName();
        this.unitPrice = cartItem.getUnitPrice();
        this.quantity = cartItem.getQuantity();
    }
}
