package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BasePricingStartegy implements PricingStartegy{

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
         return inventory.getRoom().getBaseprice();
    }

}
