package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);

        if (product == null) {
            return "redirect:/products";
        }

        cartService.addToCart(user, product, quantity);

        return "redirect:/cart";
    }

    @GetMapping
    public String cart(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        model.addAttribute("cartCount", cartService.getCartCount(user));

        return "cart";
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        cartService.updateQuantity(user, id, quantity);

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        cartService.removeFromCart(user, id);

        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clear(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        cartService.clearCart(user);

        return "redirect:/cart";
    }
}