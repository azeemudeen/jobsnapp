package com.jobsnapp.dto;

import com.jobsnapp.model.Picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserNetworkDTO {
    @NonNull private Long id;
    private Picture picture=null;
    private String currentJob;
    private String currentCompany;
}