package com.debabrata.AirBnbApllication.dto;

import com.debabrata.AirBnbApllication.entity.User;
import com.debabrata.AirBnbApllication.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    
    private  Long id;


   
    private String name;

   
    private Gender gender;


    private Integer age;

}
