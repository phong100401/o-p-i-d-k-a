package com.example.inventoryservice.queue;

import com.example.inventoryservice.dto.OrderDetailDto;
import com.example.inventoryservice.dto.OrderDto;
import com.example.inventoryservice.entity.ExportHistory;
import com.example.inventoryservice.entity.ImportHistory;
import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.enums.InventoryStatus;
import com.example.inventoryservice.repository.ExportRepository;
import com.example.inventoryservice.repository.ImportRepository;
import com.example.inventoryservice.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.example.inventoryservice.queue.Config.*;

@Service
@Log4j2
public class ConsumerService {
    @Autowired
    ExportRepository exportRepository;

    @Autowired
    ImportRepository importRepository;

    @Autowired
    ProductService productService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public void handlerInventory(@NotNull OrderDto orderDto) {
        orderDto.setQueueName(QUEUE_INVENTORY);
        if (!orderDto.validationInventory()) {
            orderDto.setMessage("CHECK_INFO_INVENTORY");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
            return;
        }
        if (orderDto.getInventoryStatus().equals(InventoryStatus.PENDING.name())) {
            handlerPendingStatus(orderDto);
            return;
        }
        if (orderDto.getInventoryStatus().equals(InventoryStatus.RETURN.name())) {
            handlerReturnStatus(orderDto);
        }
    }

    @Transactional
    public void handlerReturnStatus(@NotNull OrderDto orderDto) {
        Set<Product> products = new HashSet<>();
        Set<ImportHistory> importHistories = new HashSet<>();
        for (OrderDetailDto odt : orderDto.getOrderDetails()) {
            Product product = handlerProductNotExist(odt.getProductId(), orderDto);
            if (product == null) return;
            int quantity = odt.getQuantity();
            int unitInStock = product.getUnitInStock();
            product.setUnitInStock(unitInStock + quantity);
            importHistories.add(new ImportHistory(odt, orderDto.getOrderId()));
            products.add(product);
        }

        try {
            productService.saveAll(products);
            importRepository.saveAll(importHistories);
            orderDto.setInventoryStatus(InventoryStatus.RETURNED.name());
            orderDto.setMessage("SUCCESS_RETURN");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        } catch (Exception e) {
            orderDto.setInventoryStatus(InventoryStatus.PENDING.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_INVENTORY, orderDto);
            throw new RuntimeException(e.getMessage());
        }

    }

    private void handlerPendingStatus(@NotNull OrderDto orderDto) {
        Set<Product> products = new HashSet<>();
        Set<ExportHistory> exportHistorySet = new HashSet<>();
        for (OrderDetailDto odt : orderDto.getOrderDetails()) {
            Product product = handlerProductNotExist(odt.getProductId(), orderDto);
            if (product == null) return;
            int quantity = odt.getQuantity();
            int unitInStock = product.getUnitInStock();
            if (quantity > unitInStock) {
                orderDto.setMessage("NOT_ENOUGH_QUANTITY");
                orderDto.setInventoryStatus(InventoryStatus.OUT_OF_STOCK.name());
                rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
                return;
            }
            product.setUnitInStock(unitInStock - quantity);
            exportHistorySet.add(new ExportHistory(odt, orderDto.getOrderId()));
            products.add(product);
        }

        try {
            exportRepository.saveAll(exportHistorySet);
            productService.saveAll(products);
            orderDto.setMessage("SUCCESS_DONE");
            orderDto.setInventoryStatus(InventoryStatus.DONE.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        } catch (Exception e) {
            orderDto.setInventoryStatus(InventoryStatus.RETURN.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_INVENTORY, orderDto);
            throw new RuntimeException(e.getMessage());
        }

    }

    private @Nullable Product handlerProductNotExist(int id, OrderDto orderDto) {
        Product product = productService.findById(id);
        if (product != null) {
            return product;
        }
        orderDto.setMessage("PRODUCT_NOTFOUND");
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        return null;
    }


}
