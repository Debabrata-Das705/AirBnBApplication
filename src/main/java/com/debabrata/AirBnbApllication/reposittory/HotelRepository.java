package com.debabrata.AirBnbApllication.reposittory;

import com.debabrata.AirBnbApllication.entity.Hotel;
import com.debabrata.AirBnbApllication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByOwner(User user);
}
