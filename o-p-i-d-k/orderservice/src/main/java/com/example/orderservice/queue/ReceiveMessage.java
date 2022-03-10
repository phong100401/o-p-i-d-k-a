package com.example.orderservice.queue;

import com.example.orderservice.dto.OrderDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.orderservice.queue.Config.QUEUE_ORDER;

@Component
@Log4j2
public class ReceiveMessage {
    @Autowired
    ConsumerService consumerService;

    @RabbitListener(queues = {QUEUE_ORDER})
    public void getMessage(OrderDto orderDto){
        consumerService.handlerMessage(orderDto);
    }
}
