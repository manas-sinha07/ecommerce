package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        model.addAttribute("products", productService.getAllProducts());

        model.addAttribute("user", session.getAttribute("user"));

        return "index";
    }
}