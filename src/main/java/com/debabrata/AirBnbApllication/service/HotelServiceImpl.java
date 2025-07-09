package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.Advice.ResourceNotFoundException;
import com.debabrata.AirBnbApllication.UnAuthorisedException;
import com.debabrata.AirBnbApllication.dto.HotelDTO;
import com.debabrata.AirBnbApllication.dto.HotelInfoDto;
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
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name {}",hotelDTO.getName());
        Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);
        hotel.setActive(false);
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        log.info("Created a new hotel with name {}",hotelDTO.getName());
        return modelMapper.map( hotelRepository.save(hotel), HotelDTO.class);


    }

    @Override
    public List<HotelDTO> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());

        List<Hotel> allHotels = hotelRepository.findByOwner(user);

        return allHotels.stream().map(hotel ->modelMapper
                       .map(hotel, HotelDTO.class))
                       .collect(Collectors.toList());
    }

    @Override
    public HotelDTO getHotelById(long id) {
        log.info("Getting hotel with id {}",id);
       Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+id));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+id);
        }
        return modelMapper.map(hotel, HotelDTO.class);

    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating hotel with id {}",id);
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+id));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+id);
        }

        Hotel updatedhotel = modelMapper.map(hotelDTO, Hotel.class);
        updatedhotel.setId(id);
        Hotel savedHotel= hotelRepository.save(updatedhotel);
        HotelDTO dto= modelMapper.map(savedHotel, HotelDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public Boolean deleteHotelById(Long id) {
        log.info("Deleting hotel with id {}",id);
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+id));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+id);
        }

        for(Room room:hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());

        }
        hotelRepository.deleteById(id);

         return  true;
    }

    @Override
    @Transactional
    public void activateHotel(Long id) {
        log.info("Activating hotel with id {}",id);
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+id));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorisedException("This user does not own this hotel with id "+id);
        }

        hotel.setActive(true);
        hotelRepository.save(hotel);

        for(Room room:hotel.getRooms()){
            inventoryService.initiliseInventoryForaYear(room);
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+hotelId));
        
                List<RoomDto> rooms = hotel.getRooms().stream()
                .map(ele -> modelMapper.map(ele, RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel,HotelDTO.class),rooms);
    }
}
