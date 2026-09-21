package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartItemRepository;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void addToCart(User user, Product product, int quantity) {

        if (quantity <= 0) {
            quantity = 1;
        }

        Optional<CartItem> existingItem =
                cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {

            CartItem cartItem = existingItem.get();

            int newQuantity = cartItem.getQuantity() + quantity;

            if (newQuantity > product.getQuantity()) {
                newQuantity = product.getQuantity();
            }

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);

        } else {

            if (product.getQuantity() <= 0) {
                return;
            }

            if (quantity > product.getQuantity()) {
                quantity = product.getQuantity();
            }

            CartItem cartItem = new CartItem();

            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void updateQuantity(User user, Long cartItemId, int quantity) {

        Optional<CartItem> optionalItem =
                cartItemRepository.findById(cartItemId);

        if (optionalItem.isEmpty()) {
            return;
        }

        CartItem cartItem = optionalItem.get();

        if (!cartItem.getUser().getId().equals(user.getId())) {
            return;
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        if (quantity > cartItem.getProduct().getQuantity()) {
            quantity = cartItem.getProduct().getQuantity();
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);
    }

    public void removeFromCart(User user, Long cartItemId) {

        Optional<CartItem> optionalItem =
                cartItemRepository.findById(cartItemId);

        if (optionalItem.isEmpty()) {
            return;
        }

        CartItem cartItem = optionalItem.get();

        if (!cartItem.getUser().getId().equals(user.getId())) {
            return;
        }

        cartItemRepository.delete(cartItem);
    }

    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    public double getCartTotal(User user) {

        List<CartItem> items = cartItemRepository.findByUser(user);

        double total = 0;

        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        return total;
    }

    public int getCartCount(User user) {

        List<CartItem> items = cartItemRepository.findByUser(user);

        int count = 0;

        for (CartItem item : items) {
            count += item.getQuantity();
        }

        return count;
    }
}