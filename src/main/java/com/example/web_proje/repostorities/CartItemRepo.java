package com.example.web_proje.repostorities;

import com.example.web_proje.entities.CartItemEntity;
import com.example.web_proje.entities.ProductEntity;
import com.example.web_proje.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItemEntity,Long> {
    List<CartItemEntity> findByBuyerId(Long buyerId);
    Optional<CartItemEntity> findByBuyerAndProduct(UserEntity buyer, ProductEntity product);
    void deleteAllByBuyer_Id(Long buyerId);
    List<CartItemEntity> findAllByBuyer_Id(Long buyerId);
}
