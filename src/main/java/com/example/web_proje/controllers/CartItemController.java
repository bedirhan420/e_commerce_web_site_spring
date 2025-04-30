package com.example.web_proje.controllers;

import com.example.web_proje.dtos.CartItemDTO;
import com.example.web_proje.dtos.ProductDTO;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.services.implementation.CartItemService;
import com.example.web_proje.services.implementation.ProductService;
import com.example.web_proje.services.implementation.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@AllArgsConstructor
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;
    private ProductService productService;

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
        Map<Long, Integer> productStocks = new HashMap<>();
        double totalPrice = 0.0;

        for (CartItemDTO item : cartItems) {
            Optional<ProductDTO> product = productService.findProductById(item.getProductId());
            String productName = product.map(ProductDTO::getName).orElse("Bilinmeyen Ürün");
            int stock = product.map(ProductDTO::getStock).orElse(0);

            productNames.put(item.getProductId(), productName);
            productStocks.put(item.getProductId(), stock);

            totalPrice += item.getPrice() * item.getQuantity();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("productNames", productNames);
        model.addAttribute("productStocks", productStocks);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/view";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long cartItemId, @RequestParam Integer quantity,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        CartItemDTO cartItemDTO = cartItemService.findCartItemById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı!"));

        Optional<ProductDTO> productOpt = productService.findProductById(cartItemDTO.getProductId());
        if (productOpt.isPresent()) {
            ProductDTO product = productOpt.get();
            if (quantity > product.getStock()) {
                model.addAttribute("errorMessage", "Stok yetersiz! Maksimum " + product.getStock() + " adet ekleyebilirsiniz.");
                return viewCartAlternate(model, userDetails);
            }
        }

        cartItemDTO.setQuantity(quantity);
        cartItemService.updateCartItem(cartItemDTO);
        return "redirect:/cart/view";
    }

    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        cartItemService.clearCart(user.getId());

        model.addAttribute("successMessage", "Alışveriş başarıyla tamamlandı!");
        return viewCartAlternate(model, userDetails);
    }
}
