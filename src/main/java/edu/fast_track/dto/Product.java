package edu.fast_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private int id;
    private int qty;
    private String name;
    private String description;
    private double price;
    private String image;
    private int category;
}
