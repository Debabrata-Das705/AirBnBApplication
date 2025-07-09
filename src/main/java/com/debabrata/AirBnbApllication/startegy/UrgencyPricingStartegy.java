package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.debabrata.AirBnbApllication.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UrgencyPricingStartegy implements PricingStartegy{

     private final PricingStartegy wrapped;

     @Override
     public BigDecimal calculatePrice(Inventory inventory) {
          BigDecimal price = wrapped.calculatePrice(inventory);

          LocalDate today = LocalDate.now();

          if(!inventory.getDate().isBefore(today) &&inventory.getDate().isBefore(today.plusDays(7))){
               price=price.multiply(BigDecimal.valueOf(1.15));
          }
          return price;
     }

}
