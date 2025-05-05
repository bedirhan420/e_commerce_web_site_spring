package com.example.web_proje.services.implementation;

import com.example.web_proje.entities.UserEntity;
import com.example.web_proje.repostorities.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService { //Kullanıcı Doğrulama Servisi
    /*
    Bu sınıf, Spring Security'nin UserDetailsService arayüzünü uygular.
    Kullanıcı adıyla veritabanından kullanıcıyı çekerek doğrulama sağlar.
    */

    /*
    Bu yapı çalışabilmesi için:
        UserEntity: Kullanıcı bilgilerini tutan bir JPA Entity sınıfı olmalı.
        UserRepo: findByUsername() metodunu içeren bir Spring Data JPA repository sınıfı olmalı.
    */
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Login attempt for username: " + username);

        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User found: " + user.getUsername() + " - " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }
}
