package com.example.web_proje.controllers;

import com.example.web_proje.dtos.CartItemDTO;
import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.services.implementation.CartItemService;
import com.example.web_proje.services.implementation.ProductService;
import com.example.web_proje.services.implementation.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;
    private ProductService productService;

    public CartItemController(CartItemService cartItemService,UserService userService,ProductService productService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService=productService;
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        try {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            cartItemService.newCartItem(user.getId(), id);
            return "redirect:/cart/view";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/products/list";
        }
    }

    @GetMapping("/view")
    public String viewCartAlternate(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        List<CartItemDTO> cartItems = cartItemService.findAllCartItemsByUser(user.getId());
        Map<Long, String> productNames = new HashMap<>();

        for (CartItemDTO item : cartItems) {
            Optional<ProductDTO> product = productService.findProductById(item.getProductId());
            String productName = product.map(ProductDTO::getName).orElse("Bilinmeyen Ürün");
            productNames.put(item.getProductId(), productName);
        }

        System.out.println(productNames);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("productNames", productNames);

        return "cart/view";
    }


    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long cartItemId, @RequestParam Integer quantity) {
        CartItemDTO cartItemDTO = cartItemService.findCartItemById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı!"));

        cartItemDTO.setQuantity(quantity);
        cartItemService.updateCartItem(cartItemDTO);

        return "redirect:/cart/view";
    }

    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart/view";
    }
}
