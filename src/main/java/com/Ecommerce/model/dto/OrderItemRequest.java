package com.Ecommerce.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {}
