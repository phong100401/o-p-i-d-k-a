package com.example.paymentservice.queue;

import com.example.paymentservice.dto.OrderDto;
import com.example.paymentservice.entity.TransactionHistory;
import com.example.paymentservice.entity.Wallet;
import com.example.paymentservice.enums.PaymentStatus;
import com.example.paymentservice.enums.PaymentType;
import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.repository.TransactionRepository;
import com.example.paymentservice.service.WalletService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.example.paymentservice.queue.Config.*;

@Component
@Log4j2
public class ConsumerService {
    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public void handerPayment(@NotNull OrderDto orderDto) {
        orderDto.setQueueName(QUEUE_PAY);
        if (!orderDto.validationPayment()) {
            orderDto.setMessage("Check info payment");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
            return;
        }
        if (orderDto.getPaymentStatus().equals(PaymentStatus.REFUND.name())) {
            handlerOrderRefund(orderDto);
            return;
        }
        if (orderDto.getPaymentStatus().equals(PaymentStatus.UNPAID.name())) {
            handlerOrderUnpaid(orderDto);
        }
    }

    @Transactional
    void handlerOrderRefund(OrderDto orderDto){
        Wallet wallet = checkWalletExist(orderDto);
        if(wallet == null) return;
        TransactionHistory history = TransactionHistory.TransactionHistoryBuilder.aTransactionHistory()
                .withReceiverId(orderDto.getUserId())
                .withSenderId(1)
                .withOrderId(orderDto.getOrderId())
                .withAmount(orderDto.getTotalPrice())
                .withPaymentType(PaymentType.REFUND.name())
                .build();
        try {
            wallet.setBalance(wallet.getBalance() + orderDto.getTotalPrice());
            history.setStatus(TransactionStatus.SUCCESS.name());
            history.setMessage("REFUND SUCCESS");
            orderDto.setPaymentStatus(PaymentStatus.REFUNDED.name());
            walletService.save(wallet);
            transactionRepository.save(history);

            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        }catch (Exception e){
            history.setStatus(TransactionStatus.FAIL.name());
            transactionRepository.save(history);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, orderDto);
            throw new RuntimeException(e.getMessage());
        }
    }

    void handlerOrderUnpaid(OrderDto orderDto){
        Wallet wallet = checkWalletExist(orderDto);
        if(wallet == null) return;

        double totalPrice = orderDto.getTotalPrice();
        double balance = wallet.getBalance();

        if(totalPrice > balance){
            orderDto.setMessage("Not enough balance");
            orderDto.setPaymentStatus(PaymentStatus.FAIL.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
            return;
        }

        TransactionHistory history = TransactionHistory.TransactionHistoryBuilder.aTransactionHistory()
                .withSenderId(orderDto.getUserId())
                .withReceiverId(1)
                .withOrderId(orderDto.getOrderId())
                .withAmount(orderDto.getTotalPrice())
                .withPaymentType(PaymentType.SENDING.name())
                .build();
        try {
            wallet.setBalance(balance - totalPrice);
            history.setStatus(TransactionStatus.SUCCESS.name());
            history.setMessage("PAID");
            orderDto.setPaymentStatus(PaymentStatus.PAID.name());
            orderDto.setMessage("PAID");
            walletService.save(wallet);
            transactionRepository.save(history);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        }catch (Exception e){
            history.setStatus(TransactionStatus.FAIL.name());
            transactionRepository.save(history);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, orderDto);
            throw new RuntimeException(e.getMessage());
        }
    }

    private @Nullable Wallet checkWalletExist(@NotNull OrderDto orderDto) {
        Wallet wallet = walletService.findWalletByUserId(orderDto.getUserId());
        if(wallet == null){
            orderDto.setMessage("user not found");
            orderDto.setPaymentStatus(PaymentStatus.FAIL.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
            return null;
        }
        return wallet;
    }
}

