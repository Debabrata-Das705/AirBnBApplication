package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.dto.HotelDTO;
import com.debabrata.AirBnbApllication.dto.HotelInfoDto;

import java.util.List;

public interface HotelService {

    HotelDTO createNewHotel(HotelDTO hotelDTO);

    List<HotelDTO> getAllHotels();

    HotelDTO getHotelById(long id);

    HotelDTO updateHotelById(Long id,HotelDTO hotelDTO);

    Boolean deleteHotelById(Long id);

    void activateHotel(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
