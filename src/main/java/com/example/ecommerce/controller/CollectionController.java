package com.example.ecommerce.controller;

import com.example.ecommerce.service.CategoryService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CollectionController {

    private final CategoryService categoryService;

    public CollectionController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/collections")
    public String collections(Model model) {

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "collections";
    }
}