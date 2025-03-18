package com.example.web_proje.dtos;

public class CartItemDTO {
    private Long id;
    private Long productId;
    private Long buyerId;
    private Integer quantity;
    private Double price;  // Add price field

    public CartItemDTO() {}

    public CartItemDTO(Long id, Long productId, Long buyerId, Integer quantity, Double price) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
