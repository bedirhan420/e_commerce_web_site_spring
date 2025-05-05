package com.example.web_proje.controllers;

import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.mappers.UserMapper;
import com.example.web_proje.services.implementation.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(@AuthenticationPrincipal UserDTO user) {
        /* @AuthenticationPrincipal => Spring Security tarafından sağlanan bir anotasyondur ve oturumda (session'da) giriş yapmış kullanıcıya ait bilgileri
         doğrudan bir controller metoduna parametre olarak enjekte etmeyi sağlar.*/
        if (user != null) {
            return "redirect:/products/list";
        }
        return "login";
    }


    @GetMapping("/me")
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity entity = userService.findByUsername(userDetails.getUsername());
        UserDTO userDTO = UserMapper.toDTO(entity);
        model.addAttribute("user", userDTO);
        return "profile";
    }
}
