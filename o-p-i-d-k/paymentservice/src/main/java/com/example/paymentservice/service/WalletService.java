package com.example.paymentservice.service;

import com.example.paymentservice.dto.TransactionDto;
import com.example.paymentservice.entity.TransactionHistory;
import com.example.paymentservice.entity.Wallet;

import javax.transaction.Transactional;

public interface WalletService {
    Wallet save(Wallet wallet);

    @Transactional
    TransactionDto transfer(TransactionHistory history);

    Wallet findWalletByUserId(int userId);
}
