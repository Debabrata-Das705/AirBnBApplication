package com.debabrata.AirBnbApllication.controller;

import com.debabrata.AirBnbApllication.dto.BookingDto;
import com.debabrata.AirBnbApllication.dto.HotelDTO;
import com.debabrata.AirBnbApllication.dto.HotelReportDto;
import com.debabrata.AirBnbApllication.service.BookingService;
import com.debabrata.AirBnbApllication.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;
    private  final BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelDTO> createNewHotel(@RequestBody HotelDTO hotelDTO){
       HotelDTO hoteldto= hotelService.createNewHotel(hotelDTO);
       return  new ResponseEntity<>(hoteldto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        return new ResponseEntity<>(hotelService.getAllHotels(),HttpStatus.OK);
    }

    @GetMapping("/{hotel_id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long hotel_id) {
        HotelDTO hotelDTO = hotelService.getHotelById(hotel_id);
        return  new ResponseEntity<>(hotelDTO,HttpStatus.OK);
    }

    @PutMapping("/{hotel_id}")
    public ResponseEntity<HotelDTO> updateHotelById(@PathVariable Long hotel_id,@RequestBody HotelDTO hotelDTO) {
        HotelDTO hoteldto = hotelService.updateHotelById(hotel_id,hotelDTO);
        return  new ResponseEntity<>(hoteldto,HttpStatus.OK);
    }

    @DeleteMapping("/{hotel_id}")
    public ResponseEntity<Boolean> deleteHotelById(@PathVariable Long hotel_id) {
       return new ResponseEntity<>( hotelService.deleteHotelById(hotel_id),HttpStatus.OK);
    }

    @PatchMapping("/{hotel_id}")
    public ResponseEntity<Void> activateHotelById(@PathVariable Long hotel_id) {
        hotelService.activateHotel(hotel_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{hotelId}/bookings")
  //  @Operation(summary = "Get all bookings of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
   // @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }

}
