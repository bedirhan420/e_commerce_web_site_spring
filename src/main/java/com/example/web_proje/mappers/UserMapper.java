package com.example.web_proje.mappers;

import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.entities.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setProductIds(user.getProducts() != null ?
                user.getProducts().stream().map(p -> p.getId()).collect(Collectors.toList())
                : null);
        return dto;
    }

    public static UserEntity toEntity(UserDTO dto) {
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }
}
