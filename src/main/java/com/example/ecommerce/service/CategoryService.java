package com.example.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(Long id, Category category) {

        Category existing = categoryRepository.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(category.getName());
            return categoryRepository.save(existing);
        }

        return null;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}