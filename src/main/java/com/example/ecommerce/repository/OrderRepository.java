package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);
}