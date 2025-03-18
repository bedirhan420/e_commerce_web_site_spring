package com.example.web_proje.services.implementation;

import com.example.web_proje.dtos.CartItemDTO;
import com.example.web_proje.entities.CartItemEntity;
import com.example.web_proje.entities.ProductEntity;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.mappers.CartItemMapper;
import com.example.web_proje.repostorities.CartItemRepo;
import com.example.web_proje.repostorities.ProductRepo;
import com.example.web_proje.repostorities.UserRepo;
import com.example.web_proje.services.interfaces.ICartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService implements ICartItem {

    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    public CartItemService(CartItemRepo cartItemRepo, ProductRepo productRepo, UserRepo userRepo) {
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Override
    public CartItemDTO newCartItem(Long userId, Long productId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı!"));

        Optional<CartItemEntity> existingCartItem = cartItemRepo.findByBuyerAndProduct(user, product);
        CartItemEntity cartItem;

        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItemEntity();
            cartItem.setBuyer(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setPrice(product.getPrice());
        }

        cartItem = cartItemRepo.save(cartItem);
        return CartItemMapper.toDTO(cartItem);
    }

    @Override
    public List<CartItemDTO> findAllCartItemsByUser(Long userId) {
        return cartItemRepo.findByBuyerId(userId)
                .stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartItemDTO> findCartItemById(Long id) {
        return cartItemRepo.findById(id).map(CartItemMapper::toDTO);
    }

    @Override
    public CartItemDTO updateCartItem(CartItemDTO cartItemDTO) {
        CartItemEntity cartItem = cartItemRepo.findById(cartItemDTO.getId())
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı!"));

        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepo.save(cartItem);

        return CartItemMapper.toDTO(cartItem);
    }

    @Override
    public void deleteCartItem(Long id) {
        if (!cartItemRepo.existsById(id)) {
            throw new RuntimeException("Silinecek sepet öğesi bulunamadı!");
        }
        cartItemRepo.deleteById(id);
    }
}

