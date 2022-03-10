package com.example.orderservice.service;

import com.example.orderservice.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CartServiceImp implements CartService{

    public static HashMap<Integer, CartItem> cartHashMap = new HashMap<>();

    @Override
    public HashMap<Integer, CartItem> addToCart(CartItem cartItem1) {
        CartItem item = cartHashMap.putIfAbsent(cartItem1.getProductId(), cartItem1);
        if(item != null){
            item.setQuantity(item.getQuantity() + 1);
        }
        return cartHashMap;
    }

    @Override
    public void clear() {
        cartHashMap.clear();
    }

    @Override
    public HashMap<Integer, CartItem> getDetail() {
        return cartHashMap;
    }

    @Override
    public HashMap<Integer, CartItem> updateCart(int productId, int quantity) {
        CartItem cartItem = cartHashMap.get(productId);
        if(quantity <= 0 || cartItem == null){
            throw new RuntimeException("không tìm thấy sản phẩm trong giỏ hàng");
        }
        cartItem.setQuantity(quantity);
        return cartHashMap;
    }
}
