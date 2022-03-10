package com.example.orderservice.service;

import com.example.orderservice.entity.CartItem;

import java.util.HashMap;

public interface CartService {
    HashMap<Integer, CartItem> addToCart(CartItem cartItem1);

    void clear();

    HashMap<Integer, CartItem> getDetail();

    HashMap<Integer, CartItem> updateCart(int productId, int quantity);
}
