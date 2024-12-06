package edu.fast_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class FullOrderDetails {
    private Integer orderId;
    private Integer cost;
    private LocalDate date;
    private Integer status;
    private CustomerDetails userDetails;
    private List<ProductDetails> productDetails;
}
