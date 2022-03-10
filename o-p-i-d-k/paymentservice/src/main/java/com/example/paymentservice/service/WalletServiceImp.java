package com.example.paymentservice.service;

import com.example.paymentservice.dto.TransactionDto;
import com.example.paymentservice.entity.TransactionHistory;
import com.example.paymentservice.entity.Wallet;
import com.example.paymentservice.enums.PaymentType;
import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.exception.NotFoundException;
import com.example.paymentservice.repository.TransactionRepository;
import com.example.paymentservice.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImp implements WalletService{
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public TransactionDto transfer(TransactionHistory history) {
        TransactionDto dto = new TransactionDto();
        TransactionHistory historySave = TransactionHistory.TransactionHistoryBuilder.aTransactionHistory()
                .withSenderId(history.getSenderId())
                .withReceiverId(history.getReceiverId())
                .withMessage(history.getMessage())
                .withPaymentType(PaymentType.SENDING.name())
                .build();
        try {
            if(history.getAmount() <= 0){
                historySave.setStatus(TransactionStatus.FAIL.name());
                transactionRepository.save(historySave);
                throw new RuntimeException("Check info payment");
            }
            Wallet walletSender = walletRepository.findWalletByUserId(history.getSenderId());
            Wallet walletReceiver = walletRepository.findWalletByUserId(history.getReceiverId());

            if(walletSender == null) throw new NotFoundException("User not found");
            if(walletReceiver == null) throw new NotFoundException("User not found");
            if(walletSender.getBalance() < 0) throw new RuntimeException("Not enough balance");

            walletSender.setBalance(walletSender.getBalance() - history.getAmount());
            walletReceiver.setBalance(walletReceiver.getBalance() + history.getAmount());
            dto.setSender(walletSender.getName());
            dto.setReceiver(walletReceiver.getName());
            dto.setMessage(history.getMessage());
            dto.setAmount(history.getAmount());

            walletRepository.save(walletSender);
            walletRepository.save(walletReceiver);
            transactionRepository.save(historySave);
        }catch (Exception e){
            historySave.setStatus(TransactionStatus.FAIL.name());
            transactionRepository.save(historySave);
            throw new RuntimeException(e.getMessage());
        }
        return dto;
    }

    @Override
    public Wallet findWalletByUserId(int userId) {
        return walletRepository.findWalletByUserId(userId);
    }
}
