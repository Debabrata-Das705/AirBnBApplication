package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;
import com.debabrata.AirBnbApllication.entity.Inventory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class HolidayPricingStartegy implements PricingStartegy{

     private final PricingStartegy wrapped;

     @Override
     public BigDecimal calculatePrice(Inventory inventory) {
         BigDecimal price = wrapped.calculatePrice(inventory);

         boolean isTodayHoliday = true;//call an api or cheak with local date

         if(isTodayHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
         }

         return price;
     }
     

}
