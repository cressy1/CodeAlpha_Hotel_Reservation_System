package com.cressy.MWHotelReservations.repositories;

import com.cressy.MWHotelReservations.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Rooms, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Rooms r")
    List<String> findDistinctRoomTypes();
    @Query("SELECT r FROM Rooms r " +
            "WHERE r.roomType LIKE %:roomType% " +
            "AND r.id NOT IN (" +
            "  SELECT res.rooms.id FROM Reservations res " +
            "  WHERE ((res.checkInDate <= :checkOutDate) AND (res.checkOutDate >= :checkInDate))" +
            ")")

    List<Rooms> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
