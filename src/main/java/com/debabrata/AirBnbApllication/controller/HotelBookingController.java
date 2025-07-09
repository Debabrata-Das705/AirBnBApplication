package com.debabrata.AirBnbApllication.controller;

import com.debabrata.AirBnbApllication.dto.GuestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.debabrata.AirBnbApllication.dto.BookingDto;
import com.debabrata.AirBnbApllication.dto.BookingRequest;
import com.debabrata.AirBnbApllication.service.BookingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initiliseBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initiliseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initatePayment(@PathVariable Long bookingId){
        String sessionUrl = bookingService.initatePayment(bookingId);
        return ResponseEntity.ok(Map.of("SessionUrl",sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
    bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}/status")
    public ResponseEntity<String> getBookingStatus(@PathVariable Long bookingId){
    
        return ResponseEntity.ok(bookingService.getBookingStatus(bookingId));
    }


}
