package com.example.paymentservice.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "walléts")
@ToString
@Entity
public class Wallet {
    @Id
    private int userId;
    private double balance;
    private String name;
}
