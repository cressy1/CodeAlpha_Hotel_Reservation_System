package com.cressy.MWHotelReservations.services.ServiceImplementations;

import com.cressy.MWHotelReservations.dto.PaymentDto;
import com.cressy.MWHotelReservations.entity.Reservations;
import com.cressy.MWHotelReservations.entity.Rooms;
import com.cressy.MWHotelReservations.entity.TransactionEntity;
import com.cressy.MWHotelReservations.enums.TransactionStatus;
import com.cressy.MWHotelReservations.exceptions.InvalidReservationRequestException;
import com.cressy.MWHotelReservations.exceptions.ResourceNotFoundException;
import com.cressy.MWHotelReservations.exceptions.TransactionNotFoundException;
import com.cressy.MWHotelReservations.repositories.ReservationRepository;
import com.cressy.MWHotelReservations.repositories.TransactionRepository;
import com.cressy.MWHotelReservations.services.ReservationService;
import com.cressy.MWHotelReservations.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final TransactionRepository transactionRepository;

    public List<Reservations> getAllBookingsByRoomId(Long id) {
        return reservationRepository.findByRoomsId(id);
    }

    @Override
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public String saveReservation(Long id, Reservations reservationsRequest) {
        if (reservationsRequest.getCheckOutDate().isBefore(reservationsRequest.getCheckInDate())) {
            throw new InvalidReservationRequestException("Check-in date must come before check-out date");
        }
        Rooms room = roomService.getRoomById(id).get();
        List<Reservations> existingReservations = room.getReservations();
        boolean roomIsAvailable = roomIsAvailable(reservationsRequest, existingReservations);
        if (roomIsAvailable) {
            room.addReservation(reservationsRequest);
            reservationRepository.save(reservationsRequest);
        } else{
            throw new InvalidReservationRequestException("Sorry!!!, This room is not available for the selected date");
        }
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(reservationsRequest.getId());
        transaction.setPrice(room.getRoomPrice());
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        return reservationsRequest.getReservationConfirmationCode();

    }


    @Override
    public String validatePayment(PaymentDto paymentDto) {
       TransactionEntity transaction = transactionRepository.findById(paymentDto.getTransactionId())
               .orElseThrow(()-> new InvalidReservationRequestException("Invalid reservation"));

        transaction.setReferenceId(paymentDto.getReservations().getReservationConfirmationCode());
        transaction.setStatus(TransactionStatus.CONFIRMED);

        transactionRepository.save(transaction);

        return "Payment successful";
    }



    private boolean roomIsAvailable(Reservations reservationRequest, List<Reservations> existingReservations) {
        return existingReservations.stream()
                .noneMatch(existingReservation-> reservationRequest.getCheckInDate().equals(existingReservation.getCheckInDate())
                        || reservationRequest.getCheckOutDate().isBefore(existingReservation.getCheckOutDate())
                        || (reservationRequest.getCheckInDate().isAfter(existingReservation.getCheckInDate())

                        && reservationRequest.getCheckInDate().isBefore(existingReservation.getCheckOutDate()))
                        || (reservationRequest.getCheckInDate().isBefore(existingReservation.getCheckInDate())

                        && reservationRequest.getCheckOutDate().equals(existingReservation.getCheckOutDate()))
                        || (reservationRequest.getCheckInDate().isBefore(existingReservation.getCheckInDate())

                        && reservationRequest.getCheckOutDate().isAfter(existingReservation.getCheckOutDate()))
                        || (reservationRequest.getCheckInDate().equals(existingReservation.getCheckOutDate()))
                        && reservationRequest.getCheckOutDate().equals(reservationRequest.getCheckInDate()));

    }

    @Override
    public Reservations findByReservationConfirmationCode(String confirmationCode) {
        return  reservationRepository.findByReservationConfirmationCode(confirmationCode)
                .orElseThrow(()->new ResourceNotFoundException("No reservations found with confirmation code :"
                        + confirmationCode));
    }

    @Override
    public List<Reservations> getAllReservations() {
        return reservationRepository.findAll();
    }


}
