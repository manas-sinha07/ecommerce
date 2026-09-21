package com.example.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartService cartService) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public Order placeOrder(User user,
                            String fullName,
                            String email,
                            String phone,
                            String address,
                            String city,
                            String state,
                            String pincode) {

        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            return null;
        }

        Order order = new Order();

        order.setUser(user);
        order.setFullName(fullName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setCity(city);
        order.setState(state);
        order.setPincode(pincode);
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        double total = 0;

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            int quantity = cartItem.getQuantity();

            if (product.getQuantity() < quantity) {
                return null;
            }

            double subtotal = product.getPrice() * quantity;

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(quantity);
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);

            total += subtotal;

            product.setQuantity(product.getQuantity() - quantity);

            productRepository.save(product);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateStatus(Long id, String status) {

        Order order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}