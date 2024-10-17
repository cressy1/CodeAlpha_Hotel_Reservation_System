package com.cressy.MWHotelReservations.controller;

import com.cressy.MWHotelReservations.dto.PaymentDto;
import com.cressy.MWHotelReservations.dto.ReservationResponse;
import com.cressy.MWHotelReservations.dto.RoomResponse;
import com.cressy.MWHotelReservations.entity.Reservations;
import com.cressy.MWHotelReservations.entity.Rooms;
import com.cressy.MWHotelReservations.exceptions.InvalidReservationRequestException;
import com.cressy.MWHotelReservations.exceptions.ResourceNotFoundException;
import com.cressy.MWHotelReservations.repositories.ReservationRepository;
import com.cressy.MWHotelReservations.services.ReservationService;
import com.cressy.MWHotelReservations.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final RoomService roomService;


    @GetMapping("/all-bookings")
    public ResponseEntity<List<ReservationResponse>> getAllBookings() {
        List<Reservations> bookings = reservationService.getAllReservations();
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservations reservations : bookings) {
            ReservationResponse response = getReservationResponse(reservations);
            reservationResponses.add(response);
        }
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/confirmationCode/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            Reservations booking = reservationService.findByReservationConfirmationCode(confirmationCode);
            ReservationResponse bookingResponse = getReservationResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody Reservations reservations) {
        try {
            String confirmationCode =
                    reservationService.saveReservation(roomId, reservations);
            return ResponseEntity.ok(
                    "Room booked successfully, Your booking confirmation code is " + confirmationCode);
        } catch (InvalidReservationRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PostMapping("/payment")
    public ResponseEntity<String> payment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(reservationService.validatePayment(paymentDto));
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        reservationService.cancelReservation(bookingId);
    }

    private ReservationResponse getReservationResponse(Reservations reservations) {
        Rooms theRoom = roomService.getRoomById(reservations.getRooms().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new ReservationResponse(
                reservations.getId(),
                reservations.getCheckInDate(),
                reservations.getCheckOutDate(),
                reservations.getGuestFullName(),
                reservations.getGuestEmail(),
                reservations.getNumOfAdults(),
                reservations.getNumOfChildren(),
                reservations.getTotalNumOfGuest(),
                reservations.getReservationConfirmationCode(), room);
    }
}
