package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.Advice.ResourceNotFoundException;
import com.debabrata.AirBnbApllication.UnAuthorisedException;
import com.debabrata.AirBnbApllication.dto.GuestDto;
import com.debabrata.AirBnbApllication.dto.HotelReportDto;
import com.debabrata.AirBnbApllication.entity.*;
import com.debabrata.AirBnbApllication.enums.BookingStatus;
import com.debabrata.AirBnbApllication.reposittory.*;
import com.debabrata.AirBnbApllication.startegy.PricingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.debabrata.AirBnbApllication.dto.BookingDto;
import com.debabrata.AirBnbApllication.dto.BookingRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutService checkOutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private   String frontendurl;


    @Override
    @Transactional
    public BookingDto initiliseBooking(BookingRequest bookingRequest) {

        log.info("Initilising booking hotel :{} ,room :{}, date :{}",bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate());
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id "+bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id "+bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvilableInventory(
                room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;

        if(inventoryList.size()!=daysCount){
            throw new IllegalStateException("Room is not available anymore.");
        }

        //Reserve the rooms/ Update the bookedCount of the inventories.
        inventoryRepository.initBooking(room.getId(), bookingRequest.getCheckInDate(),
         bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());



        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .cheakInDate(bookingRequest.getCheckInDate())
                .cheakOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(totalPrice)
                .build();

      Booking  newbooking= bookingRepository.save(booking);
      return modelMapper.map(newbooking, BookingDto.class);

    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guests for booking with id: {}",bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id "+bookingId));

        User user =getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with booking id "+booking.getId());
        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired.");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests.");
        }


        for(GuestDto guestDto:guestDtoList){
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest=guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking =bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);


    }

    @Override
    @Transactional
    public String initatePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id "+bookingId));
        User user=getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with booking id "+user.getId());
        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired.");
        }

       String sessionUrl = checkOutService.getCheckOutSession(booking,frontendurl+"/payments/success", frontendurl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);


        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayments(Event event) {

        if("checkout.session.completed".equals(event.getType())){
            Session session =(Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if(session == null) return;

            String sessionId = session.getId();
            Booking booking = bookingRepository.findByPaymentSessionId(sessionId)
                    .orElseThrow(() ->new ResourceNotFoundException("Booking not found with seddion id :"+sessionId));
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheakInDate(),booking.getCheakOutDate(),booking.getRoomsCount());
            inventoryRepository.confirmBooking(booking.getRoom().getId(),booking.getCheakInDate(),booking.getCheakOutDate(),booking.getRoomsCount());

            log.info("Successfully confirmed the booking for id {}",booking.getId());
        }

    }

     private Session retrieveSessionFromEvent(Event event) {
        log.info("inside  retrieveSessionFromEvent");
        try {

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if (deserializer.getObject().isPresent()) {
                return (Session) deserializer.getObject().get();
            } else {
                String rawJson = event.getData().getObject().toJson();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(rawJson);
                String sessionId = jsonNode.get("id").asText();

                return Session.retrieve(sessionId);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to retrieve session data");
        }
    }

    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public void cancelBooking(Long bookingId) {
         Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id "+bookingId));
        User user=getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with booking id "+user.getId());
        }

       if(booking.getBookingStatus()!=BookingStatus.CONFIRMED){
        throw new IllegalStateException("Only comfirmed booking can be cancled.");
       }

       booking.setBookingStatus(BookingStatus.CANCLLED);
       bookingRepository.save(booking);

       inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheakInDate(),booking.getCheakOutDate(),booking.getRoomsCount());
       inventoryRepository.cancelBooking(booking.getRoom().getId(),booking.getCheakInDate(),booking.getCheakOutDate(),booking.getRoomsCount());

    //Handle refund

       try {

        Session session = Session.retrieve(booking.getPaymentSessionId());
        RefundCreateParams refundParams = RefundCreateParams.builder()
        .setPaymentIntent(session.getPaymentIntent())
        .build();
        Refund.create(refundParams);

    } catch (StripeException e) {
        e.printStackTrace();
    }

    }

    @Override
    public String getBookingStatus(Long bookingId) {
          Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id "+bookingId));

        User user =getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with booking id "+booking.getId());
        }

        return booking.getBookingStatus().name();

    }
    @Override
    public List<BookingDto> getAllBookingsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not " +
                "found with ID: "+hotelId));
        User user = getCurrentUser();

        log.info("Getting all booking for the hotel with ID: {}", hotelId);

        if(!user.equals(hotel.getOwner())) throw new AccessDeniedException("You are not the owner of hotel with id: "+hotelId);

        List<Booking> bookings = bookingRepository.findByHotel(hotel);

        return bookings.stream()
                .map((element) -> modelMapper.map(element, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not " +
                "found with ID: "+hotelId));
        User user = getCurrentUser();

        log.info("Generating report for hotel with ID: {}", hotelId);

        if(!user.equals(hotel.getOwner())) throw new AccessDeniedException("You are not the owner of hotel with id: "+hotelId);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Booking> bookings = bookingRepository.findByHotelAndCreatedAtBetween(hotel, startDateTime, endDateTime);

        Long totalConfirmedBookings = bookings
                .stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        BigDecimal totalRevenueOfConfirmedBookings = bookings.stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgRevenue = totalConfirmedBookings == 0 ? BigDecimal.ZERO :
                totalRevenueOfConfirmedBookings.divide(BigDecimal.valueOf(totalConfirmedBookings), RoundingMode.HALF_UP);

        return new HotelReportDto(totalConfirmedBookings, totalRevenueOfConfirmedBookings, avgRevenue);
    }

    @Override
    public List<BookingDto> getMyBookings() {
        User user = getCurrentUser();

        return bookingRepository.findByUser(user)
                .stream().
                map((element) -> modelMapper.map(element, BookingDto.class))
                .collect(Collectors.toList());
    }




}
