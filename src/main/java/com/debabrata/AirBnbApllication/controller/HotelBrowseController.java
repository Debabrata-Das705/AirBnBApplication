package com.debabrata.AirBnbApllication.controller;

import com.debabrata.AirBnbApllication.dto.HotelSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debabrata.AirBnbApllication.dto.HotelInfoDto;
import com.debabrata.AirBnbApllication.dto.HotelPriceDto;
import com.debabrata.AirBnbApllication.service.HotelService;
import com.debabrata.AirBnbApllication.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){

       var page = inventoryService.searchHotels(hotelSearchRequest);
       return ResponseEntity.ok(page);

    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));

    }
}
