package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SurdgePricingStartegy implements PricingStartegy{

    private final PricingStartegy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgefactor());
    }

}
