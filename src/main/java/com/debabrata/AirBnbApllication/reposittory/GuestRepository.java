package com.debabrata.AirBnbApllication.reposittory;

import com.debabrata.AirBnbApllication.entity.Guest;
import com.debabrata.AirBnbApllication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest,Long> {
    List<Guest> findByUser(User user);


}
