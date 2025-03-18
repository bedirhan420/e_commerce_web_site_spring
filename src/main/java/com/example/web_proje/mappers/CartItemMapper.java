package com.example.web_proje.mappers;

import com.example.web_proje.dtos.CartItemDTO;
import com.example.web_proje.entities.CartItemEntity;

public class CartItemMapper {
    public static CartItemDTO toDTO(CartItemEntity cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProduct() != null ? cartItem.getProduct().getId() : null);
        dto.setBuyerId(cartItem.getBuyer() != null ? cartItem.getBuyer().getId() : null);
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        return dto;
    }

    public static CartItemEntity toEntity(CartItemDTO dto) {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(dto.getId());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setPrice(dto.getPrice());
        return cartItem;
    }
}
