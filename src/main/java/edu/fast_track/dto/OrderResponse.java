package edu.fast_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class OrderResponse {
    private int orderId;
    private LocalDate date;
    private int status;
    private String number;
}
