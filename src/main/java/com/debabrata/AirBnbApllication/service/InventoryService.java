package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.dto.*;
import com.debabrata.AirBnbApllication.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    public void initiliseInventoryForaYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);

}

