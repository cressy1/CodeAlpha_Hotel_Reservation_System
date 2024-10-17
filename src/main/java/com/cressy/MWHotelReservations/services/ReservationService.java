package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.dto.PaymentDto;
import com.cressy.MWHotelReservations.entity.Reservations;

import java.util.List;

public interface ReservationService {

    List<Reservations> getAllBookingsByRoomId(Long id);
    void cancelReservation(Long id);

    String saveReservation(Long id, Reservations reservationsRequest);

    Reservations findByReservationConfirmationCode(String confirmationCode);

    List<Reservations> getAllReservations();

    String validatePayment(PaymentDto paymentDto);

}
