package com.example.web_proje.services.implementation;

import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.entities.CartItemEntity;
import com.example.web_proje.entities.ProductEntity;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.mappers.ProductMapper;
import com.example.web_proje.repostorities.CartItemRepo;
import com.example.web_proje.repostorities.ProductRepo;
import com.example.web_proje.repostorities.UserRepo;
import com.example.web_proje.services.interfaces.IProduct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductService implements IProduct {

    private ProductRepo productRepo;
    private UserRepo userRepo;
    private CartItemRepo cartItemRepo;

    @Override
    public ProductDTO newProduct(ProductDTO productDTO) {
        ProductEntity product = ProductMapper.toEntity(productDTO);

        UserEntity seller = userRepo.findById(productDTO.getSellerId())
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı!"));

        product.setSeller(seller);

        if (productDTO.getImage() != null && productDTO.getImage().length > 0) {
            product.setImage(productDTO.getImage());
        }

        ProductEntity savedProduct = productRepo.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        return productRepo.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findProductById(Long id) {
        return productRepo.findById(id)
                .map(ProductMapper::toDTO);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        ProductEntity product = productRepo.findById(productDTO.getId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı!"));

        // Validating inputs before updating
        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            throw new RuntimeException("Ürün adı boş olamaz!");
        }
        if (productDTO.getPrice() <= 0) {
            throw new RuntimeException("Fiyat geçerli olmalıdır!");
        }

        // Updating fields
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());

        UserEntity seller = userRepo.findById(productDTO.getSellerId())
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı!"));
        product.setSeller(seller);

        if (productDTO.getCartItemIds() != null) {
            List<CartItemEntity> updatedCartItems = productDTO.getCartItemIds().stream()
                    .map(cartItemId -> cartItemRepo.findById(cartItemId)
                            .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı!")))
                    .collect(Collectors.toList());
            product.setCartItems(updatedCartItems);
        }

        // Only update image if changed
        if (productDTO.getImage() != null && productDTO.getImage().length > 0) {
            product.setImage(productDTO.getImage());
        }

        ProductEntity updatedProduct = productRepo.save(product);
        return ProductMapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Silinecek ürün bulunamadı!");
        }
        productRepo.deleteById(id);
    }

    @Override
    public List<ProductDTO> searchProducts(String name, Double minPrice, Double maxPrice) {
        List<ProductEntity> products;

        if (name != null && minPrice != null && maxPrice != null) {
            products = productRepo.findByNameContainingIgnoreCaseAndPriceBetween(name, minPrice, maxPrice);
        } else if (name != null && minPrice != null) {
            products = productRepo.findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(name, minPrice);
        } else if (name != null && maxPrice != null) {
            products = productRepo.findByNameContainingIgnoreCaseAndPriceLessThanEqual(name, maxPrice);
        } else if (name != null) {
            products = productRepo.findByNameContainingIgnoreCase(name);
        } else if (minPrice != null && maxPrice != null) {
            products = productRepo.findByPriceBetween(minPrice, maxPrice);
        } else if (minPrice != null) {
            products = productRepo.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            products = productRepo.findByPriceLessThanEqual(maxPrice);
        } else {
            products = productRepo.findAll();
        }

        return products.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

}
