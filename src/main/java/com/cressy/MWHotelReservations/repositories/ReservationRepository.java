package com.cressy.MWHotelReservations.repositories;

import com.cressy.MWHotelReservations.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findByRoomsId(Long id);

    Optional<Reservations> findByReservationConfirmationCode(String confirmationCode);
}
