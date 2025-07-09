package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Inventory;

import lombok.RequiredArgsConstructor;


public interface PricingStartegy {


    BigDecimal calculatePrice(Inventory inventory);

}
