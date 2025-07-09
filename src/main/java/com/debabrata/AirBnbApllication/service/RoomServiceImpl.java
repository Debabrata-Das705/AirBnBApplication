package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.Advice.ResourceNotFoundException;
import com.debabrata.AirBnbApllication.UnAuthorisedException;
import com.debabrata.AirBnbApllication.dto.RoomDto;
import com.debabrata.AirBnbApllication.entity.Hotel;
import com.debabrata.AirBnbApllication.entity.Room;
import com.debabrata.AirBnbApllication.entity.User;
import com.debabrata.AirBnbApllication.reposittory.HotelRepository;
import com.debabrata.AirBnbApllication.reposittory.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.debabrata.AirBnbApllication.util.AppUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(RoomDto roomdto, Long hotelId) {
        log.info("Creating new room with id {}",hotelId);
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+hotelId));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+hotelId);
        }

       Room newRoom = modelMapper.map(roomdto, Room.class);
       newRoom.setHotel(hotel);
       roomRepository.save(newRoom);
        if(hotel.getActive()){
            inventoryService.initiliseInventoryForaYear(newRoom);
        }
       return modelMapper.map(newRoom, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel id {}",hotelId);
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+hotelId));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public RoomDto getRoomById(Long id) {
        log.info("Getting  room by id {}",id);
        Room room= roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id "+id));



        return modelMapper.map(room, RoomDto.class);

    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {
        log.info("Deleting  room by id {}",id);
        Room room= roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id "+id));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+room.getHotel().getId());
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(id);
        
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = getCurrentUser();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        modelMapper.map(roomDto, room);
        room.setId(roomId);

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);
    }

}
