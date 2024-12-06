package edu.fast_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetails {
    private String name;
    private Integer category;
    private String image;
    private Integer numberOfItems;
    private Integer price;
}
