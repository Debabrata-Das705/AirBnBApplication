package com.debabrata.AirBnbApllication.dto;

import com.debabrata.AirBnbApllication.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private  Long id;
    private String name;
    private  String email;
    private Gender gender;
    private LocalDate dateOfBirth;

}
