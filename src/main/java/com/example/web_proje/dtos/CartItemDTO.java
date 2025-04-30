package com.example.web_proje.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private Long productId;
    private Long buyerId;
    private Integer quantity;
    private Double price;
}
