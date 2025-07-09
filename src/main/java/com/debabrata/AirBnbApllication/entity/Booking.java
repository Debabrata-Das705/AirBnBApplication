package com.debabrata.AirBnbApllication.entity;

import com.debabrata.AirBnbApllication.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(nullable = false)
    private Integer roomsCount;

    @Column(nullable = false)
    private LocalDate cheakInDate;

    @Column(nullable = false)
    private LocalDate cheakOutDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    private Set<Guest> guests;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;

    @Column(unique = true)
    private String paymentSessionId;
}
