package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OccupancyPricingStartegy implements PricingStartegy{

    private final PricingStartegy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        double occupancyRate = inventory.getBookedCount()/ inventory.getTotalcount();
        if(occupancyRate > 0.8){
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
        
    }

}
