package com.example.ecommerce.controller;
import org.springframework.http.MediaType;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService,
            CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // =========================
    // CUSTOMER
    // =========================

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String category,
            Model model) {

        if (category != null && !category.isBlank()) {
            model.addAttribute(
                    "products",
                    productService.getAllProducts()
                            .stream()
                            .filter(product -> product.getCategory() != null &&
                                    product.getCategory().getName()
                                            .equalsIgnoreCase(category))
                            .toList());

            model.addAttribute("selectedCategory", category);

        } else {
            model.addAttribute(
                    "products",
                    productService.getAllProducts());

            model.addAttribute("selectedCategory", null);
        }

        return "products";
    }

    @GetMapping("/products/details/{id}")
    public String productDetails(@PathVariable Long id,
            Model model) {

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "product-details";
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin/products")
    public String adminProducts(HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "products",
                productService.getAllProducts());

        return "admin-products";
    }

    @GetMapping("/admin/products/add")
    public String addProductPage(HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("product", new Product());
        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "add-product";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            if (!imageFile.isEmpty()) {
                product.setImage(imageFile.getBytes());
            }

            productService.saveProduct(product);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/products/add?error=image";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editProductPage(@PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/admin/products";
        }

        model.addAttribute("product", product);

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "edit-product";
    }

    @PostMapping("/admin/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
            @ModelAttribute Product product,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        productService.updateProduct(id, product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }

    private boolean isAdmin(HttpSession session) {

        Object userObject = session.getAttribute("user");

        if (userObject == null) {
            return false;
        }

        com.example.ecommerce.entity.User user = (com.example.ecommerce.entity.User) userObject;

        return "ADMIN".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/product/{id}/image")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {

        Product product = productService.getProductById(id);

        if (product == null || product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(product.getImage());
    }
}