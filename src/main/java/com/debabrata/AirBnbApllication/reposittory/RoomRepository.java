package com.debabrata.AirBnbApllication.reposittory;

import com.debabrata.AirBnbApllication.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
