package com.debabrata.AirBnbApllication.dto;

import com.debabrata.AirBnbApllication.entity.HotelContacInfo;
import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContacInfo hotelContacInfo;

}
