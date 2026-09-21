package com.example.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product product) {

        Product existing = productRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            existing.setQuantity(product.getQuantity());
            existing.setImage(product.getImage());
            existing.setCategory(product.getCategory());

            return productRepository.save(existing);
        }

        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}