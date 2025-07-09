package com.debabrata.AirBnbApllication.dto;

import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class HotelSearchRequest {

   
    private String city;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private Integer roomsCount;

    private Integer page;
    private Integer size=10;
}
