package com.debabrata.AirBnbApllication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "hotel_room_date",
                columnNames = {"Hotel_id","Room_id","date"}
        )
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition ="INTEGER DEFAULT 0" )
    private Integer bookedCount;

    @Column(nullable = false, columnDefinition ="INTEGER DEFAULT 0" )
    private Integer reservedCount;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer totalcount;

    @Column(nullable = false, precision = 5,scale = 2)
    private BigDecimal surgefactor;

    @Column(nullable = false, precision =10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean closed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
