package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.entity.Booking;

public interface CheckOutService {

    String getCheckOutSession(Booking booking, String successUrl, String failUrl);
}
