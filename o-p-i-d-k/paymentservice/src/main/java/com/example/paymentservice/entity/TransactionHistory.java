package com.example.paymentservice.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "transaction_history")
@Entity
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "sender is required")
    private int senderId;

    @NotNull(message = "Receiver is required")
    private int receiverId;

    private int orderId;
    private String paymentType;

    @NotNull(message = "Amount is required")
    private double amount;
    private String status;

    private String message;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public TransactionHistory(int senderId, int orderId, String paymentType, double amount) {
        this.senderId = senderId;
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.createAt = LocalDateTime.now();
    }


    public static final class TransactionHistoryBuilder {
        private int id;
        private int senderId;
        private int receiverId;
        private int orderId;
        private String paymentType;
        private double amount;
        private String status;
        private String message;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        private TransactionHistoryBuilder() {
        }

        public static TransactionHistoryBuilder aTransactionHistory() {
            return new TransactionHistoryBuilder();
        }

        public TransactionHistoryBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public TransactionHistoryBuilder withSenderId(int senderId) {
            this.senderId = senderId;
            return this;
        }

        public TransactionHistoryBuilder withReceiverId(int receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public TransactionHistoryBuilder withOrderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public TransactionHistoryBuilder withPaymentType(String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public TransactionHistoryBuilder withAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionHistoryBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public TransactionHistoryBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public TransactionHistoryBuilder withCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public TransactionHistoryBuilder withUpdateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public TransactionHistory build() {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setId(id);
            transactionHistory.setSenderId(senderId);
            transactionHistory.setReceiverId(receiverId);
            transactionHistory.setOrderId(orderId);
            transactionHistory.setPaymentType(paymentType);
            transactionHistory.setAmount(amount);
            transactionHistory.setStatus(status);
            transactionHistory.setMessage(message);
            transactionHistory.setCreateAt(createAt);
            transactionHistory.setUpdateAt(updateAt);
            return transactionHistory;
        }
    }
}

