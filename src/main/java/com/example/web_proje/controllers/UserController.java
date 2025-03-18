package com.example.web_proje.controllers;

import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.services.implementation.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        if (user != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<UserDTO> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_detail"; // Display user details
        }
        return "redirect:/users/all"; // Redirect to users list if not found
    }

    @GetMapping("/update/{id}")
    public String showUpdateUserPage(@PathVariable Long id, Model model) {
        Optional<UserDTO> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "update_user"; // Return the update user page
        }
        return "redirect:/users/all"; // Redirect if user not found
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/users/all"; // Redirect to user list after update
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users/all"; // Redirect to user list after deletion
    }

    @GetMapping("/me")
    public String getCurrentUser(@AuthenticationPrincipal UserDTO currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "profile";
    }
}
