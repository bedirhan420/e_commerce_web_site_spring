package com.example.web_proje.repostorities;

import com.example.web_proje.entities.ProductEntity;
import com.example.web_proje.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findBySeller(UserEntity seller);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    List<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductEntity> findByNameContainingIgnoreCaseAndPriceBetween(String name, Double minPrice, Double maxPrice);
}
