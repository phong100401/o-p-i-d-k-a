package com.example.paymentservice.repository;

import com.example.paymentservice.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer>, JpaSpecificationExecutor<TransactionHistory> {
    TransactionHistory findTransactionHistoriesByOrderId(int orderId);

    @Query(value = "SELECT * FROM transaction_history " +
            "WHERE transaction_history.sender_id = 451691 " +
            "OR transaction_history.receiver_id = 451691 " +
            "ORDER BY transaction_history.id DESC", nativeQuery = true)
    List<TransactionHistory> findTransactionHistoryBySenderId(int senderId);
}
