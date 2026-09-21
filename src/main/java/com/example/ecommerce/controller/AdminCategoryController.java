package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.service.CategoryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public String categories(HttpSession session,
                             Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "admin-categories";
    }


    @GetMapping("/add")
    public String addCategoryPage(HttpSession session,
                                  Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("category", new Category());

        return "add-category";
    }


    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        categoryService.saveCategory(category);

        return "redirect:/admin/categories";
    }


    @GetMapping("/edit/{id}")
    public String editCategoryPage(@PathVariable Long id,
                                   HttpSession session,
                                   Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/admin/categories";
        }

        model.addAttribute("category", category);

        return "edit-category";
    }


    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category category,
                                 HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        categoryService.updateCategory(id, category);

        return "redirect:/admin/categories";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        categoryService.deleteCategory(id);

        return "redirect:/admin/categories";
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