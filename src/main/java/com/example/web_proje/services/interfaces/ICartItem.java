package com.example.web_proje.services.interfaces;

import com.example.web_proje.dtos.CartItemDTO;

import java.util.List;
import java.util.Optional;

public interface ICartItem {
    CartItemDTO newCartItem(Long buyerId, Long productId);
    List<CartItemDTO> findAllCartItemsByUser(Long userId);
    Optional<CartItemDTO> findCartItemById(Long id);
    CartItemDTO updateCartItem(CartItemDTO cartItemDTO);
    void deleteCartItem(Long id);
    void clearCart(Long userId);
}
