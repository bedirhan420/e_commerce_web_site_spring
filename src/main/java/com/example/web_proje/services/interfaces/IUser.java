package com.example.web_proje.services.interfaces;

import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUser {
    UserDTO registerUser(UserDTO userDTO);
    Optional<UserDTO> findUserById(Long id);
    UserEntity findByUsername(String username);
}
