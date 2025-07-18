package com.debabrata.AirBnbApllication.dto;


import com.debabrata.AirBnbApllication.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
