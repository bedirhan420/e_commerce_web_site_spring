package com.example.web_proje.repostorities;

import com.example.web_proje.entities.CartItemEntity;
import com.example.web_proje.entities.ProductEntity;
import com.example.web_proje.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItemEntity,Long> {
    List<CartItemEntity> findByBuyerId(Long buyerId);
    Optional<CartItemEntity> findByBuyerAndProduct(UserEntity buyer, ProductEntity product);

}
