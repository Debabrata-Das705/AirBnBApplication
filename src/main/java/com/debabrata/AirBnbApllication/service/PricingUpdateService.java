package com.debabrata.AirBnbApllication.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.entity.Hotel;
import com.debabrata.AirBnbApllication.entity.HotelMinPrice;
import com.debabrata.AirBnbApllication.entity.Inventory;
import com.debabrata.AirBnbApllication.reposittory.HotelMinPriceRepository;
import com.debabrata.AirBnbApllication.reposittory.HotelRepository;
import com.debabrata.AirBnbApllication.reposittory.InventoryRepository;
import com.debabrata.AirBnbApllication.startegy.PricingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;


    //Sheduler to update the inventory and hotelminprice every hour

    @Scheduled(cron = "0 0 * * * *")
    public void updatePrice(){
        int page = 0;
        int batchSize=100;

        while(true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if(hotelPage.isEmpty()){
                break;
            }

            hotelPage.getContent().forEach(hotel ->updateHotelPrices(hotel));

            page++;
        }
    }

    private void updateHotelPrices(Hotel  hotel){

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);
        updateInventoryPrices(inventoryList);

        updateHotelMinPrice(hotel, inventoryList, startDate,endDate);

    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate,
            LocalDate endDate) {

                Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(
                    Inventory::getDate,
                    Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().orElse(BigDecimal.ZERO)));

                //Prepare hotelprice entity in bulk

                List<HotelMinPrice> hotelPrices = new ArrayList<>();
                dailyMinPrice.forEach((date,price) ->{
                    HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
                    hotelPrice.setPrice(price);
                    hotelPrices.add(hotelPrice);
                });

                //Save all hotel price entities in bulk

                hotelMinPriceRepository.saveAll(hotelPrices);
        
    }

    private void updateInventoryPrices(List<Inventory> inventoryList){
        inventoryList.forEach(inventory ->{
            BigDecimal dynamicPrice=pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }

}
