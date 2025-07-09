package com.debabrata.AirBnbApllication.reposittory;

import com.debabrata.AirBnbApllication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

   Optional<User> findByEmail(String email);


}

