package com.example.ecommerce.controller;

import com.example.ecommerce.service.OrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public String orders(HttpSession session,
                         Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orders",
                orderService.getAllOrders()
        );

        return "admin-orders";
    }


    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        orderService.updateStatus(id, status);

        return "redirect:/admin/orders";
    }


    private boolean isAdmin(HttpSession session) {

        Object userObject = session.getAttribute("user");

        if (userObject == null) {
            return false;
        }

        com.example.ecommerce.entity.User user =
                (com.example.ecommerce.entity.User) userObject;

        return "ADMIN".equalsIgnoreCase(user.getRole());
    }
}