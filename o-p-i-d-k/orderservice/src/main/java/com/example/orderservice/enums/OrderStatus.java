package com.example.orderservice.enums;

public enum OrderStatus {
    CANCEL, //huỷ đơn
    PENDING, // chờ xử lý
    PROCESSING, // đang xử lý
    DONE, // đã giao hàng
    REFUND // trả hàng
}
