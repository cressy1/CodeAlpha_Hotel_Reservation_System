package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.entity.Rooms;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Rooms addNewRoom(MultipartFile file,
                     String roomType,
                     BigDecimal roomPrice) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    List<Rooms> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;

    void deleteRoom(Long roomId);

    Rooms updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);

    Optional<Rooms> getRoomById(Long roomId);

    List<Rooms> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
