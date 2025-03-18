package com.example.web_proje.mappers;

import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.entities.ProductEntity;

import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO toDTO(ProductEntity product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setSellerId(product.getSeller() != null ? product.getSeller().getId() : null);
        dto.setCartItemIds(product.getCartItems() != null ?
                product.getCartItems().stream().map(cartItem -> cartItem.getId()).collect(Collectors.toList())
                : null);
        return dto;
    }

    public static ProductEntity toEntity(ProductDTO dto) {
        ProductEntity product = new ProductEntity();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return product;
    }
}