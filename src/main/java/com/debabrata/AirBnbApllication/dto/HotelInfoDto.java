package com.debabrata.AirBnbApllication.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelInfoDto {

    private HotelDTO hotelDTO;
    private List<RoomDto> rooms;
}
