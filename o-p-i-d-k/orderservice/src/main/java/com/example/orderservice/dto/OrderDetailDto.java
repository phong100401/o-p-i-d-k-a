package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderDetail;
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

    public OrderDetailDto(OrderDetail orderDetail){
        this.productId = orderDetail.getProductId();
        this.orderId = orderDetail.getOrderId();
        this.productName = orderDetail.getProductName();
        this.quantity = orderDetail.getQuantity();
        this.unitPrice = orderDetail.getUnitPrice();
    }
    public boolean validation(){
        return this.orderId != 0 && this.productId != 0 && this.productName != null
                && this.quantity > 0 && this.unitPrice > 0;
    }
}
