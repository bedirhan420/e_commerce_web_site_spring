package com.example.web_proje.controllers;

import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.services.implementation.CartItemService;
import com.example.web_proje.services.implementation.ProductService;
import com.example.web_proje.services.implementation.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private  ProductService productService;
    private  UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listProducts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<ProductDTO> products = productService.findAllProducts();
        model.addAttribute("products", products);

        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
        }

        return "product/list";
    }

    @GetMapping("/new")
    public String newProductForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userService.findByUsername(userDetails.getUsername()).getRole().name().equals("SELLER")) {
            return "redirect:/products?error=Unauthorized";
        }

        model.addAttribute("product", new ProductDTO());
        return "product/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute ProductDTO productDTO, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Product DTO: " + productDTO);
        if (userDetails == null) {
            return "redirect:/products?error=Unauthorized";
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        if (!"SELLER".equals(user.getRole().name())) {
            return "redirect:/products?error=Unauthorized";
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

        if (!"SELLER".equals(user.getRole().name()) || !product.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        model.addAttribute("product", product);
        return "product/edit";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/products/list?error=Unauthorized";
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        if (!"SELLER".equals(user.getRole().name())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        ProductDTO existingProduct = productService.findProductById(productDTO.getId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (!existingProduct.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }

        productDTO.setSellerId(user.getId());
        productService.updateProduct(productDTO);

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
        if (!"SELLER".equals(user.getRole().name()) || !product.getSellerId().equals(user.getId())) {
            return "redirect:/products/list?error=Unauthorized";
        }
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) Double minPrice,
                                 @RequestParam(required = false) Double maxPrice,
                                 Model model) {
        List<ProductDTO> products = productService.searchProducts(name, minPrice, maxPrice);
        model.addAttribute("products", products);
        return "product/list";
    }


}
