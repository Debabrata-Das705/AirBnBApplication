package com.debabrata.AirBnbApllication.service;


import com.debabrata.AirBnbApllication.dto.GuestDto;

import java.util.List;

public interface GuestService {

        List<GuestDto> getAllGuests();

        void updateGuest(Long guestId, GuestDto guestDto);

        void deleteGuest(Long guestId);

        GuestDto addNewGuest(GuestDto guestDto);
    }


