package com.debabrata.AirBnbApllication.dto;

import com.debabrata.AirBnbApllication.entity.Hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    private Hotel hotel;
    private Double price;

}
