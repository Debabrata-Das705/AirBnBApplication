package com.debabrata.AirBnbApllication.startegy;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Inventory;

@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory){

        PricingStartegy pricingStartegy = new BasePricingStartegy();

        pricingStartegy=new SurdgePricingStartegy(pricingStartegy);
        pricingStartegy=new OccupancyPricingStartegy(pricingStartegy);
        pricingStartegy=new UrgencyPricingStartegy(pricingStartegy);
        pricingStartegy=new HolidayPricingStartegy(pricingStartegy);

        return pricingStartegy.calculatePrice(inventory);
    }

    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList){
      BigDecimal totalPrice =  inventoryList.stream()
        .map(inventory ->calculateDynamicPricing(inventory))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPrice;
    }
}
