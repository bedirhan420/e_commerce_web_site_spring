package com.example.web_proje.services.interfaces;

import com.example.web_proje.dtos.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProduct{
    ProductDTO newProduct(ProductDTO productDTO);
    List<ProductDTO> findAllProducts();
    Optional<ProductDTO> findProductById(Long id);
    ProductDTO updateProduct(ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> searchProducts(String name, Double minPrice, Double maxPrice);
    }
