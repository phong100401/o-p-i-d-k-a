package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.CartItem;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.enums.InventoryStatus;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.orderservice.queue.Config.DIRECT_EXCHANGE;
import static com.example.orderservice.queue.Config.DIRECT_SHARE_ROUTING_KEY;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public OrderDto create(Order order) {
        Order orderSave;
        try {
            orderSave = orderRepository.save(order);
            Double totalPrice = Double.valueOf(0);
            Set<OrderDetail> orderDetailsHashSet = new HashSet<>();
            for (CartItem cartItem:
                 CartServiceImp.cartHashMap.values()) {
                totalPrice = totalPrice + (Double) cartItem.getUnitPrice() * (Integer) cartItem.getQuantity();
                OrderDetail orderDetail = new OrderDetail(cartItem);
                orderDetail.setOrderId(orderSave.getId());
                orderDetailsHashSet.add(orderDetail);
            }

            if(totalPrice <= 0){
                throw new RuntimeException();
            }
            orderSave.setTotalPrice(totalPrice);
            orderSave.setCreatedAt(LocalDate.now());
            orderSave.setPaymentStatus(PaymentStatus.UNPAID.name());
            orderSave.setOrderStatus(OrderStatus.PENDING.name());
            orderSave.setInventoryStatus(InventoryStatus.PENDING.name());
            orderSave.setOrderDetails(orderDetailsHashSet);
            System.out.println(orderSave.toString());

            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_SHARE_ROUTING_KEY, new OrderDto(orderSave));
            cartService.clear();
            return new OrderDto(orderSave);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<Order> getAll(int page, int pageSize) {
        if (page <= 0) {
            page = 1;
        }
        if (pageSize < 0) {
            page = 6;
        }
        return orderRepository.findAll(PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    @Override
    public Order findById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order save(Order orderExist) {
        return orderRepository.save(orderExist);
    }

    @Override
    public List findOrderByUserId(int userId) {
        return orderRepository.findOrderByUserId(userId);
    }
}
