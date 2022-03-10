package com.example.inventoryservice.entity;

import com.example.inventoryservice.dto.OrderDetailDto;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "import_history")
@Entity
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private int quantity;
    private double unitPrice;
    private int productId;
    private LocalDate createdAt;

    public ImportHistory(@NotNull OrderDetailDto product, int orderId){
        this.orderId = orderId;
        this.productId = product.getProductId();
        this.unitPrice = product.getUnitPrice();
        this.quantity = product.getQuantity();
        this.createdAt = LocalDate.now();
        this.createdAt = LocalDate.now();
    }
}
