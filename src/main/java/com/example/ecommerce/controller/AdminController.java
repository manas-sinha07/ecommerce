package com.example.ecommerce.controller;

import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(ProductService productService,
                           CategoryService categoryService,
                           OrderService orderService,
                           UserService userService) {

        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String dashboard(HttpSession session,
                            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "productCount",
                productService.getAllProducts().size()
        );

        model.addAttribute(
                "categoryCount",
                categoryService.getAllCategories().size()
        );

        model.addAttribute(
                "orderCount",
                orderService.getAllOrders().size()
        );

        model.addAttribute(
                "customerCount",
                userService.getAllUsers()
                        .stream()
                        .filter(user ->
                                !"ADMIN".equalsIgnoreCase(user.getRole()))
                        .count()
        );

        return "admin";
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