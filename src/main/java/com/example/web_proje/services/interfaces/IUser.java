package com.example.web_proje.services.interfaces;

import com.example.web_proje.dtos.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUser {
    UserDTO registerUser(UserDTO userDTO);
    void logoutUser(); // Kullanıcı çıkışı
    List<UserDTO> findAllUsers();
    Optional<UserDTO> findUserById(Long id);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);
}
