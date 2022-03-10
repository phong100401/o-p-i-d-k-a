package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderDto create(Order order);

    Page<Order> getAll(int page, int pageSize);

    Order findById(int orderId);

    Order save(Order orderExist);

    List findOrderByUserId(int userId);
}
