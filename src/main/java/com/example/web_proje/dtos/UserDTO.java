package com.example.web_proje.dtos;

import  com.example.web_proje.entities.Role;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private List<Long> productIds;

    public UserDTO() {

    }

    public UserDTO(Long id, String username, String email,String password, Role role, List<Long> productIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password=password;
        this.role = role;
        this.productIds = productIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

}
