package edu.fast_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDetails {
    private int id;
    private String fName;
    private String lName;
    private String image;
    private String number;
    private String address;
    private String email;
}
