package com.debabrata.AirBnbApllication.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import com.debabrata.AirBnbApllication.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingDto {

    private Long id;
    private Integer roomsCount;
    private LocalDate cheakInDate;
    private LocalDate cheakOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
    private BigDecimal amount;

}
