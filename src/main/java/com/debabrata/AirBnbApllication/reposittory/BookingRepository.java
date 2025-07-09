package com.debabrata.AirBnbApllication.reposittory;

import com.debabrata.AirBnbApllication.entity.Hotel;
import com.debabrata.AirBnbApllication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.debabrata.AirBnbApllication.entity.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long>{


    Optional<Booking> findByPaymentSessionId(String sessionId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Collection<Object> findByUser(User user);

}
