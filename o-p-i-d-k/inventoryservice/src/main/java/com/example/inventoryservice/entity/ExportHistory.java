package com.example.inventoryservice.entity;

import com.example.inventoryservice.dto.OrderDetailDto;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "export_history")
@ToString
@Entity
public class ExportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderId;
    private int quantity;
    private int productId;
    private LocalDate createdAt;

    public ExportHistory(@NotNull OrderDetailDto orderDetailDto, int orderId){
        this.orderId = orderId;
        this.quantity = orderDetailDto.getQuantity();
        this.productId = orderDetailDto.getProductId();
        this.createdAt = LocalDate.now();
    }
}
