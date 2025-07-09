package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.Advice.ResourceNotFoundException;
import com.debabrata.AirBnbApllication.dto.*;
import com.debabrata.AirBnbApllication.entity.*;
import com.debabrata.AirBnbApllication.reposittory.HotelMinPriceRepository;
import com.debabrata.AirBnbApllication.reposittory.InventoryRepository;
import com.debabrata.AirBnbApllication.reposittory.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.debabrata.AirBnbApllication.util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private  final RoomRepository roomRepository;
    @Override
    public void initiliseInventoryForaYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for( ;!today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .price(room.getBaseprice())
                    .surgefactor(BigDecimal.valueOf(1))
                    .date(today)
                    .totalcount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotel for {} city, from{} to {}",hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        if(hotelSearchRequest.getStartDate() == null || hotelSearchRequest.getEndDate()==null) 
        throw new IllegalArgumentException("Start date or end date can not be null");
        
       Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());

       long dateCount=ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;

       Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelWithAvilableInventory(hotelSearchRequest.getCity(),
       hotelSearchRequest.getStartDate(),
         hotelSearchRequest.getEndDate(),
         hotelSearchRequest.getRoomsCount(),
         dateCount,
        pageable);

       return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting All inventory by room for room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element,
                        InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating All inventory by room for room with id: {} between date range: {} - {}", roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(), updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor());
    }


}
