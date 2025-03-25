package com.example.web_proje.services.implementation;

import com.example.web_proje.dtos.UserDTO;
import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.mappers.UserMapper;
import com.example.web_proje.repostorities.UserRepo;
import com.example.web_proje.services.interfaces.IUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService implements IUser {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepo.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        validateEmail(userDTO.getEmail());
        validatePassword(userDTO.getPassword());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setRole(userDTO.getRole());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userEntity = userRepo.save(userEntity);
        return UserMapper.toDTO(userEntity);
    }


    @Override
    public List<UserDTO> findAllUsers() {
        return userRepo.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findUserById(Long id) {
        return userRepo.findById(id).map(UserMapper::toDTO);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        return userRepo.findById(userDTO.getId())
                .map(userEntity -> {
                    if (!userEntity.getUsername().equals(userDTO.getUsername()) &&
                            userRepo.existsByUsername(userDTO.getUsername())) {
                        throw new IllegalArgumentException("Username is already taken.");
                    }

                    if (!userEntity.getEmail().equals(userDTO.getEmail()) &&
                            userRepo.existsByEmail(userDTO.getEmail())) {
                        throw new IllegalArgumentException("Email is already in use.");
                    }

                    validateEmail(userDTO.getEmail());

                    userEntity.setUsername(userDTO.getUsername());
                    userEntity.setEmail(userDTO.getEmail());
                    userEntity.setRole(userDTO.getRole());

                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        validatePassword(userDTO.getPassword());
                        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }

                    return UserMapper.toDTO(userRepo.save(userEntity));
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public UserEntity findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validatePassword(String password) {
        if (password.length() != 8 ||
                !password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must be 8 characters long including numbers");
        }
    }
}
