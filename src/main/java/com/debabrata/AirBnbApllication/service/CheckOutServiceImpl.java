package com.debabrata.AirBnbApllication.service;

import com.debabrata.AirBnbApllication.entity.Booking;
import com.debabrata.AirBnbApllication.entity.User;
import com.debabrata.AirBnbApllication.reposittory.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService{

    private final BookingRepository bookingRepository;

    @Override
    public String getCheckOutSession(Booking booking, String successUrl, String failUrl) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Customer customer = Customer.create(
                    CustomerCreateParams.builder()
                            .setName(user.getName())
                            .setEmail(user.getEmail())
                            .build()
            );
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(1000)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName()+" : "+booking.getRoom().getType())
                                                                    .setDescription("Booking id :"+booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            booking.setPaymentSessionId(session.getId());

            bookingRepository.save(booking);

            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }


    }
}
