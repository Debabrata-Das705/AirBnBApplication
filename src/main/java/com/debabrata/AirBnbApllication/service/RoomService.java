package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(RoomDto roomdto, Long hotelId);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long id);

    void deleteRoomById(Long id);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);

}
