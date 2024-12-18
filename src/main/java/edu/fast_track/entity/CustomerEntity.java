package edu.fast_track.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "`user`")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fName;
    private String lName;
    private String image;
    private String number;
    private String address;
    private String email;
    private String password;

    @Column(name = "`delete`")
    private int delete;
}
