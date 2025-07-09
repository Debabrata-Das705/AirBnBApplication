package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.dto.ProfileUpdateRequestDto;
import com.debabrata.AirBnbApllication.dto.UserDto;
import com.debabrata.AirBnbApllication.entity.User;

public interface UserService {

    User getUserById(Long id);


    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
