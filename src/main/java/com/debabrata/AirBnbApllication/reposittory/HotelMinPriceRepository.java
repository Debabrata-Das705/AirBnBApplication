package com.debabrata.AirBnbApllication.reposittory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.debabrata.AirBnbApllication.dto.HotelPriceDto;
import com.debabrata.AirBnbApllication.entity.Hotel;
import com.debabrata.AirBnbApllication.entity.HotelMinPrice;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long>{

     @Query("""
            SELECT new com.debabrata.AirBnbApllication.dto.HotelPriceDto(i.hotel, AVG(i.price))
            FROM HotelMinPrice i
            WHERE i.hotel.city= :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.hotel.active =true
            GROUP BY i.hotel
            """)
    Page<HotelPriceDto> findHotelWithAvilableInventory(
        @Param("city")String city,
        @Param("startDate")LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("roomsCount")Integer roomsCount,
        @Param("dateCount")long dateCount,
        Pageable pageable

    );

     Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);

}
