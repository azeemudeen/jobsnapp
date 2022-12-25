package com.jobsnapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

import com.jobsnapp.model.Picture;

@Data
@AllArgsConstructor
public class NetworkUserDTO {
    private Long id;
    private String name;
    private String surname;
    private String position;
    private String company;

    // IMAGE MISSING
    // private Picture picture;
}
