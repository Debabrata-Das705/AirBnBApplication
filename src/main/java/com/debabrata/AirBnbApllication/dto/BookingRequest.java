package com.debabrata.AirBnbApllication.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookingRequest {

    private Long hotelId;
    private Long roomId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkInDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkOutDate;

    private Integer roomsCount;

}
