package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String orders(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orders",
                orderService.getOrdersByUser(user)
        );

        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetails(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);

        if (order == null) {
            return "redirect:/orders";
        }

        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);

        return "order-details";
    }
}