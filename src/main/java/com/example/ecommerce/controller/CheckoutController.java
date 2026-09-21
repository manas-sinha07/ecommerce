package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService,
                              OrderService orderService) {

        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String checkout(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (cartService.getCartItems(user).isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        model.addAttribute("user", user);

        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String pincode,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.placeOrder(
                user,
                fullName,
                email,
                phone,
                address,
                city,
                state,
                pincode
        );

        if (order == null) {
            model.addAttribute("error",
                    "Some products are no longer available in the requested quantity.");

            model.addAttribute("cartItems", cartService.getCartItems(user));
            model.addAttribute("cartTotal", cartService.getCartTotal(user));
            model.addAttribute("user", user);

            return "checkout";
        }

        return "redirect:/orders/" + order.getId();
    }
}