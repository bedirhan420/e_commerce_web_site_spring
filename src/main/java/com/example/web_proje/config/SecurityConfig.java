package com.example.web_proje.config;

import com.example.web_proje.services.implementation.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration// Bu sınıfın bir konfigürasyon sınıfı olduğunu belirtir. Spring uygulaması başlarken bu sınıfı okuyarak güvenlik yapılandırmasını uygular.
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    @Bean //Spring Framework’te bir nesnenin (bean’in) Spring IoC konteyneri tarafından yönetilmesini sağlamak için kullanılan bir anotasyondur.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Güvenlik Kurallarını Tanımlar
        http
                .csrf(csrf -> csrf.disable()) // Cross-Site Request Forgery (CSRF) korumasını devre dışı bırakır.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/register", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/products/list", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );// kullanıcının oturum bilgileri HTTP session içinde saklanır, çerezlerde sadece oturum kimliği (JSESSIONID) tutulur.
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
