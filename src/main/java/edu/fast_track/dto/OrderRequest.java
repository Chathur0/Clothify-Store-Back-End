package edu.fast_track.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<CartItem> cart;
    private int totalCost;
    private Customer customer;

    @Data
    public static class CartItem {
        private int productId;
        private int quantity;
        private int price;
    }

}
