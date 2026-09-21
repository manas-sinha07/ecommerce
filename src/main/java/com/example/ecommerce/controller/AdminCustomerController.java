package com.example.ecommerce.controller;

import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCustomerController {

    private final UserService userService;

    public AdminCustomerController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin/customers")
    public String customers(HttpSession session,
                            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "customers",
                userService.getAllUsers()
        );

        return "admin-customers";
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