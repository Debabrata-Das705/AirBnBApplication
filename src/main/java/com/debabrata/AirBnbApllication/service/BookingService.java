package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.dto.BookingDto;
import com.debabrata.AirBnbApllication.dto.BookingRequest;
import com.debabrata.AirBnbApllication.dto.GuestDto;
import com.debabrata.AirBnbApllication.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {

    BookingDto initiliseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initatePayment(Long bookingId);

    void capturePayments(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}


