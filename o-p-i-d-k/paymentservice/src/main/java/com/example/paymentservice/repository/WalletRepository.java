package com.example.paymentservice.repository;

import com.example.paymentservice.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer>  {
    Wallet findWalletByUserId(int id);
}
