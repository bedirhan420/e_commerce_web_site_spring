package com.example.web_proje.controllers;

import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.enums.Role;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.services.implementation.ProductService;
import com.example.web_proje.services.implementation.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductController {

    private  ProductService productService;
    private  UserService userService;

    @GetMapping("/list")
    public String listProducts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<ProductDTO> products;

        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());

            // SELLER rolündeyse, sadece kendi ürünlerini filtreliyoruz
            if (Role.SELLER.name().equals(user.getRole().name())) {
                products = productService.findAllProducts().stream()
                        .filter(product -> product.getSellerId().equals(user.getId()))
                        .collect(Collectors.toList());
            } else {
                // BUYER rolündeyse tüm ürünleri gösteriyoruz
                products = productService.findAllProducts();
            }
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");

            products = productService.findAllProducts();
        }

        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setImageBase64(base64Image);
            }
        });
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/new")
    public String newProductForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userService.findByUsername(userDetails.getUsername()).getRole().name().equals(Role.SELLER.name())) {
            return "redirect:/products?error=Unauthorized";
        }

        model.addAttribute("product", new ProductDTO());
        return "product/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute ProductDTO productDTO,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Product DTO: " + productDTO);

        if (userDetails == null) {
            return "redirect:/products?error=Unauthorized";
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        if (!Role.SELLER.name().equals(user.getRole().name())) {
            return "redirect:/products?error=Unauthorized";
        }

        try {
            if (!imageFile.isEmpty()) {
                productDTO.setImage(imageFile.getBytes());
                System.out.println("Resim Yükleme Başarılı");
            }
            System.out.println("Resim Boş Geldi");
        } catch (IOException e) {
            System.out.println("Resim Yüklenemedi");
            return "redirect:/products?error=ImageUploadFailed";
        }

        productDTO.setSellerId(user.getId());
        productService.newProduct(productDTO);

        return "redirect:/products/list";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/products/list?error=Unauthorized";
        }

        ProductDTO product = productService.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        UserEntity user = userService.findByUsername(userDetails.getUsername());

        if (!Role.SELLER.name().equals(user.getRole().name()) || !product.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        model.addAttribute("product", product);
        return "product/edit";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam Double price,
                                @RequestParam Integer stock,
                                @RequestParam(required = false) MultipartFile image,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/products/list?error=Unauthorized";
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        if (!Role.SELLER.name().equals(user.getRole().name())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        ProductDTO existingProduct = productService.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (!existingProduct.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        try {
            if (image != null && !image.isEmpty()) {
                existingProduct.setImage(image.getBytes());
            } else {
                existingProduct.setImage(existingProduct.getImage());
            }

            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);
            existingProduct.setStock(stock);
            existingProduct.setSellerId(user.getId());

            productService.updateProduct(existingProduct);
        } catch (IOException e) {
            return "redirect:/products/list?error=ImageUploadFailed";
        }

        return "redirect:/products/list?success=Product updated successfully";
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/products/list?error=Unauthorized";
        }

        ProductDTO product = productService.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        if (!Role.SELLER.name().equals(user.getRole().name()) || !product.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) Double minPrice,
                                 @RequestParam(required = false) Double maxPrice,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        List<ProductDTO> products = productService.searchProducts(name, minPrice, maxPrice);

        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());

            if (Role.SELLER.name().equals(user.getRole().name())) {
                products = products.stream()
                        .filter(product -> product.getSellerId().equals(user.getId()))
                        .collect(Collectors.toList());
            }
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
        }

        // Görseli base64'e çevir
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setImageBase64(base64Image);
            }
        });

        model.addAttribute("products", products);
        return "product/list";
    }


}
